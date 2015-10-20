package com.jph.putils.http;

import com.jph.putils.HttpUtil;

/**
 * HttpURLConnection配置类
 * Author: JPH
 * Date: 2015/10/14 0014 13:37
 */
public class HttpConfig {
    private String cookie;
    /**是否启用application/json Content-type的标识*/
    private boolean enableJsonContentType;
    private int connectTimeout;
    private boolean useCaches ;
    /**
     * 设置是否启用application/json Content-type的标识
     * @param enableJsonContentType
     * @return
     */
    public HttpConfig enableJsonContentType(boolean enableJsonContentType) {
        this.enableJsonContentType = enableJsonContentType;
        return this;
    }

    /**
     * 设置最大的连接等待超时时间
     * @param timeoutMillis
     * @return
     */
    public HttpConfig setConnectTimeout (int timeoutMillis){
        this.connectTimeout=timeoutMillis;
        return this;
    }
    /**
     * 设置Cookie
     * @param cookie
     * @return
     */
    public HttpConfig setCookie (String cookie){
        this.cookie=cookie;
        HttpUtil.cookie=cookie;
        return this;
    }

    /**
     * 设置连接是否允许使用缓存
     * @param newValue
     * @return
     */
    public HttpConfig setUseCaches (boolean newValue){
        this.useCaches=newValue;
        return this;
    }

    public boolean isUseCaches() {
        return useCaches;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public boolean isEnableJsonContentType() {
        return enableJsonContentType;
    }
    public String getCookie() {
        return cookie;
    }
}
