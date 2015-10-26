package com.jph.putils.exception;

import com.jph.putils.http.entity.BaseResponseInfo;

/**
 * Author: JPH
 * Date: 2015/10/15 0015 11:17
 */
public class HttpException extends BaseException{
    private String errorMsg;
    private BaseResponseInfo responseInfo;

    public HttpException(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public HttpException(BaseResponseInfo responseInfo,String errorMsg) {
        super(errorMsg);
        this.responseInfo=responseInfo;
        this.errorMsg=errorMsg;
    }
    public HttpException(BaseResponseInfo responseInfo,String errorMsg,Throwable throwable) {
        super(errorMsg,throwable);
        this.responseInfo=responseInfo;
        this.errorMsg=errorMsg;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public BaseResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(BaseResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }
}
