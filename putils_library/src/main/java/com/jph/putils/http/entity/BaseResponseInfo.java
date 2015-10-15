package com.jph.putils.http.entity;

import com.jph.putils.http.HttpConfig;

/**
 * Author: JPH
 * Date: 2015/10/15 0015 11:24
 */
public class BaseResponseInfo {
    private int httpCode;
    private String responseContent;
    private String cookie;

    public BaseResponseInfo() {
    }

    public BaseResponseInfo(String cookie, int httpCode, String responseContent) {
        this.cookie = cookie;
        this.httpCode = httpCode;
        this.responseContent = responseContent;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
        HttpConfig.cookie=cookie;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }
}
