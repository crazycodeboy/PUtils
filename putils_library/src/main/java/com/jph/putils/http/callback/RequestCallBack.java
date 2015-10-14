package com.jph.putils.http.callback;

/**
 * Author: JPH
 * Date: 2015/10/14 0014 10:32
 */
public interface RequestCallBack {
    void onSuccess(String responseContent, String cookieStr);
    void onFailure(String error);
}