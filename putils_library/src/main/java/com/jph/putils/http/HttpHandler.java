package com.jph.putils.http;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jph.putils.http.callback.RequestCallBack;
import com.jph.putils.http.entity.ResponseInfo;
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
public class HttpHandler extends AsyncTask<String, Integer, ResponseInfo> {
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
    protected ResponseInfo doInBackground(String... params) {
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
    protected void onPostExecute(ResponseInfo responseInfo) {
        super.onPostExecute(responseInfo);
        if (responseInfo.isSuccess){
            callBack.onSuccess(responseInfo.responseContent,responseInfo.cookieStr);
        }else {
            callBack.onFailure(responseInfo.error);
        }
    }
    private void initConfig(HttpURLConnection conn) throws ProtocolException {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod(request.getMethod().toString());
        if (!TextUtils.isEmpty(HttpConfig.cookie))conn.setRequestProperty("Cookie",HttpConfig.cookie);
        HttpConfig config=request.getConfig();
        if (config==null)return;
        if (config.isEnableJsonContentType())conn.setRequestProperty("Content-type", "application/json");//使用application/json
    }
    private ResponseInfo onSend(boolean isWithData) {
        ResponseInfo responseInfo=new ResponseInfo();
        HttpURLConnection conn = null;
        try {
            if (!isWithData&&request.getParams()!=null)request.setUrl(Utils.genUrlWithParam(request.getParams(),request.getUrl()));
            URL url = new URL(request.getUrl());
            conn= (HttpURLConnection) url.openConnection();
            initConfig(conn);
            if (isWithData)uploadData(conn);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String cookieStr = conn.getHeaderField("Set-Cookie");
                String result = Utils.getStringFromInputStream(conn.getInputStream());
                responseInfo.responseContent=result;
                responseInfo.isSuccess=true;
                responseInfo.cookieStr=cookieStr;
                Log.i("info", "cookieStr:" + cookieStr);
                Log.i("info", "result:" + result);
            } else {
                responseInfo.error="ResponseCode:" + conn.getResponseCode();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            responseInfo.error=e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            responseInfo.error=e.getMessage();
        } finally {
            if (conn != null) conn.disconnect();
        }
        return responseInfo;
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