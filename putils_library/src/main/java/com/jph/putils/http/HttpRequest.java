package com.jph.putils.http;

import com.jph.putils.http.entity.RequestParams;

/**
 * Author: JPH
 * Date: 2015/10/14 0014 10:59
 */
public class HttpRequest {
    private HttpMethod method;
    private String url;
    private RequestParams params;
    private HttpConfig config;
    public HttpRequest(String url, HttpMethod method,RequestParams params) {
        this.url = url;
        this.method = method;
        this.params=params;
    }
    public HttpRequest(String url, HttpMethod method,RequestParams params,HttpConfig config) {
        this(url,method,params);
        this.config=config;
    }
    public HttpMethod getMethod() {
        return method;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public RequestParams getParams() {
        return params;
    }
    public HttpConfig getConfig() {
        return config;
    }

    public static enum HttpMethod {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
//        HEAD("HEAD"),
//        MOVE("MOVE"),
//        COPY("COPY"),
        DELETE("DELETE");
//        OPTIONS("OPTIONS"),
//        TRACE("TRACE"),
//        CONNECT("CONNECT");

        private final String value;
        HttpMethod(String value) {
            this.value = value;
        }
        @Override
        public String toString() {
            return this.value;
        }
    }
}
