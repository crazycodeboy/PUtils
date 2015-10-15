package com.jph.putils;

import com.jph.putils.http.HttpRequest;
import com.jph.putils.http.HttpHandler;
import com.jph.putils.http.callback.RequestCallBack;
import com.jph.putils.http.entity.BaseResponseInfo;
import com.jph.putils.http.entity.HttpException;

/**
 * 网络操作工具类
 * Author: JPH
 * Date: 2015/10/10 0010 16:55
 */
public class HttpUtil {
    public HttpUtil() {}
    /**
     * 发送请求
     * @param callBack 请求回调
     */
    public void send(HttpRequest request,RequestCallBack callBack) {
        try {
            new HttpHandler(request,callBack).execute();
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure(new HttpException(new BaseResponseInfo(),e.toString()));
        }
    }
}


