package com.jph.putils.http.entity;

/**
 * Author: JPH
 * Date: 2015/10/15 0015 11:17
 */
public class HttpException extends BaseResponseInfo{
    private String errorMsg;

    public HttpException(BaseResponseInfo info,String errorMsg) {
        super(info.getCookie(),info.getHttpCode(),info.getResponseContent());
        this.errorMsg=errorMsg;
    }
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
