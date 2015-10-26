package com.jph.putils.http;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jph.putils.HttpUtil;
import com.jph.putils.http.callback.RequestCallBack;
import com.jph.putils.http.entity.BaseResponseInfo;
import com.jph.putils.exception.HttpException;
import com.jph.putils.http.entity.ResponseInfo;
import com.jph.putils.util.HttpCodeUtil;
import com.jph.putils.util.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Author: JPH
 * Date: 2015/10/14 0014 10:37
 */
public class HttpHandler extends AsyncTask<String, Integer, Object> {
    private HttpRequest request;
    private RequestCallBack callBack;
    public HttpHandler(HttpRequest request, RequestCallBack callBack) {
        this.request = request;
        this.callBack=callBack;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Object doInBackground(String... params) {
        if (Utils.isWithData(request.getMethod())) {
            return onSend(true);
        } else {
            return onSend(false);
        }
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
    @Override
    protected void onPostExecute(Object info) {
        super.onPostExecute(info);
        if (info instanceof ResponseInfo){
            callBack.onSuccess((ResponseInfo)info);
        }else {
            callBack.onFailure((HttpException)info);
        }
    }
    private void initConfig(HttpURLConnection conn,boolean isWithData) throws ProtocolException {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        conn.setDoInput(true);
        if (isWithData)conn.setDoOutput(true);//如果需要上传数据则打开输出设置
        conn.setRequestMethod(request.getMethod().toString());
//        if (!TextUtils.isEmpty(HttpUtil.cookie))conn.setRequestProperty("Cookie",HttpUtil.cookie);
        HttpConfig config=request.getConfig();
        if (config==null)return;
        conn.setRequestProperty("Cookie",config.getCookie());
        if (config.isEnableJsonContentType())conn.setRequestProperty("Content-type", "application/json");//使用application/json
    }
    private Object onSend(boolean isWithData) {
        Object object = null;
        BaseResponseInfo responseInfo=new BaseResponseInfo();
        HttpURLConnection conn = null;
        try {
            if (!isWithData&&request.getParams()!=null)request.setUrl(Utils.genUrlWithParam(request.getParams(), request.getUrl()));
            URL url = new URL(request.getUrl());
            conn= (HttpURLConnection) url.openConnection();
            initConfig(conn, isWithData);
            if (isWithData)uploadData(conn);
            int httpCode=conn.getResponseCode();
            responseInfo.setHttpCode(httpCode);
            String cookieStr = conn.getHeaderField("Set-Cookie");
            responseInfo.setCookie(cookieStr);
            Log.i("info", "cookieStr:" + cookieStr);
            String result=null;
            if (httpCode<300) {
                result = Utils.getStringFromInputStream(conn.getInputStream());
                responseInfo.setResponseContent(result);
                object=new ResponseInfo(responseInfo);
            } else {
                result = Utils.getStringFromInputStream(conn.getErrorStream());
                responseInfo.setResponseContent(result);
                object=new HttpException(responseInfo, HttpCodeUtil.getCodeMessage(responseInfo.getHttpCode()));
            }
            Log.i("info", "result:" + result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            object=new HttpException(responseInfo,e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            object=new HttpException(responseInfo,e.toString());
        } finally {
            if (conn != null) conn.disconnect();
        }
        return object;
    }

    /**
     * 上传数据
     * @param conn
     * @throws IOException
     */
    private void uploadData(HttpURLConnection conn) throws IOException {
        OutputStream outputStream = conn.getOutputStream();
        String paramData;
        HttpConfig config=request.getConfig();
        if (config!=null&&config.isEnableJsonContentType()){
            paramData=Utils.mapToJsonStr(request.getParams());
        }else {
            paramData=Utils.genFormData(request.getParams());
        }
        outputStream.write(paramData.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}