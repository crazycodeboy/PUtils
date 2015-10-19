
package com.jph.putils.exception;

public class HttpException extends BaseException {
    private static final long serialVersionUID = 1L;

    private int exceptionCode;
    private String entity;
    /**
     * 
     * @return The http response httpEntity
     */
    public String getEntity() {
        return entity;
    }
    public HttpException() {
    }

    public HttpException(String detailMessage) {
        super(detailMessage);
    }

    public HttpException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public HttpException(Throwable throwable) {
        super(throwable);
    }

    /**
     * @param exceptionCode The http response status code, 0 if the http request error and has no response.
     */
    public HttpException(int exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    /**
     * @param exceptionCode The http response status code, 0 if the http request error and has no response.
     * @param detailMessage
     */
    public HttpException(int exceptionCode, String detailMessage) {
        super(detailMessage);
        this.exceptionCode = exceptionCode;
    }

    /**
     * @param exceptionCode The http response status code, 0 if the http request error and has no response.
     * @param detailMessage
     * @param throwable
     */
    public HttpException(int exceptionCode, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.exceptionCode = exceptionCode;
    }

    /**
     * @param exceptionCode The http response status code, 0 if the http request error and has no response.
     * @param throwable
     */
    public HttpException(int exceptionCode, Throwable throwable) {
        super(throwable);
        this.exceptionCode = exceptionCode;
    }

    /**
     * 对于非正常状态下，返回HTTP entity
     * @param exceptionCode 
     * @param detailMessage
     * @param entity  HTTP entity
     * @author JPG
     * @Date 2015-7-1 下午2:08:37
     */
    public HttpException(int exceptionCode, String detailMessage, String entity) {
    	this(exceptionCode, detailMessage);
    	this.entity=entity;
	}
	/**
     * @return The http response status code, 0 if the http request error and has no response.
     */
    public int getExceptionCode() {
        return exceptionCode;
    }
}
