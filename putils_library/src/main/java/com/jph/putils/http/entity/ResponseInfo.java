package com.jph.putils.http.entity;

/**
 * Author: JPH
 * Date: 2015/10/14 0014 10:32
 */
public class ResponseInfo extends BaseResponseInfo{
    public ResponseInfo(BaseResponseInfo info) {
        super(info.getCookie(),info.getHttpCode(),info.getResponseContent());
    }
}