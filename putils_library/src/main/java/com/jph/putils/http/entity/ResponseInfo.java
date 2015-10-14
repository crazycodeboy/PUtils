package com.jph.putils.http.entity;

import com.jph.putils.http.HttpConfig;

/**
 * Author: JPH
 * Date: 2015/10/14 0014 10:32
 */
public class ResponseInfo{
    public boolean isSuccess;
    public String responseContent;
    public String cookieStr;
    public  String error;
    public void setCookieStr(String cookieStr) {
        this.cookieStr = cookieStr;
        HttpConfig.cookie=cookieStr;
    }
}