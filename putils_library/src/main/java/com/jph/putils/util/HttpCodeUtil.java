package com.jph.putils.util;

/**
 * HTTP code 解析工具类
 * Author: JPH
 * Date: 2015/10/15 0015 20:14
 */
public class HttpCodeUtil {
    /**
     * 根据HTTP code 返回code对应的信息
     * @param httpCode
     * @return
     */
    public static String getCodeMessage(int httpCode) {
        if (httpCode < 400) return getlowCodeMessage(httpCode);
        return getHightCodeMessage(httpCode);
    }

    private static String getlowCodeMessage(int httpCode) {
        String codeMessage;
        switch (httpCode) {
            case 100:
                codeMessage = "100:Continue";
                break;
            case 101:
                codeMessage = "101:Switching Protocols";
                break;
            case 102:
                codeMessage = "102:Processing";
                break;
            case 200:
                codeMessage = "200:OK";
                break;
            case 201:
                codeMessage = " 201:Created";
                break;
            case 202:
                codeMessage = "202:Accepted";
                break;
            case 203:
                codeMessage = "203:Non-Authoritative Information";
                break;
            case 204:
                codeMessage = "204:No Content";
                break;
            case 205:
                codeMessage = " 205:Reset Content";
                break;
            case 206:
                codeMessage = "206:Partial Content";
                break;
            case 207:
                codeMessage = "207:Multi-Status";
                break;
            case 300:
                codeMessage = "300:Multiple Choices";
                break;
            case 301:
                codeMessage = " 301:Moved Permanently";
                break;
            case 302:
                codeMessage = "302:Move temporarily";
                break;
            case 303:
                codeMessage = "303:See Other";
                break;
            case 304:
                codeMessage = "304:Not Modified";
                break;
            case 305:
                codeMessage = " 305:Use Proxy";
                break;
            case 306:
                codeMessage = "306:Switch Proxy";
                break;
            case 307:
                codeMessage = "307:Temporary Redirect";
                break;
            default:
                codeMessage = "";
                break;
        }
        return codeMessage;
    }

    private static String getHightCodeMessage(int httpCode) {
        String codeMessage;
        switch (httpCode) {
            case 400:
                codeMessage = "400:Bad Request";
                break;
            case 401:
                codeMessage = "401:Unauthorized";
                break;
            case 402:
                codeMessage = "402:Payment Required";
                break;
            case 403:
                codeMessage = "403:Forbidden";
                break;
            case 404:
                codeMessage = "404:Not Found";
                break;
            case 405:
                codeMessage = "405:Method Not Allowed";
                break;
            case 406:
                codeMessage = "406:Not Acceptable";
                break;
            case 407:
                codeMessage = "407:Proxy Authentication Required";
                break;
            case 408:
                codeMessage = "408:Request Timeout";
                break;
            case 409:
                codeMessage = "409:Conflict";
                break;
            case 410:
                codeMessage = "410:Gone";
                break;
            case 411:
                codeMessage = "411:Length Required";
                break;
            case 412:
                codeMessage = "412:Precondition Failed";
                break;
            case 413:
                codeMessage = "413:Request Entity Too Large";
                break;
            case 414:
                codeMessage = "413:Request-URI Too Long";
                break;
            case 415:
                codeMessage = "415:Unsupported Media Type";
                break;
            case 416:
                codeMessage = "416:Requested Range Not Satisfiable";
                break;
            case 417:
                codeMessage = "417:Expectation Failed";
                break;
            case 421:
                codeMessage = "421:There are too many connections from your internet address";
                break;
            case 422:
                codeMessage = "422:Unprocessable Entity";
                break;
            case 423:
                codeMessage = "423:Locked";
                break;
            case 424:
                codeMessage = "424:Failed Dependency";
                break;
            case 425:
                codeMessage = "425:Unordered Collection";
                break;
            case 426:
                codeMessage = "426:Upgrade Required";
                break;
            case 449:
                codeMessage = "449:Retry With";
                break;
            case 500:
                codeMessage = "500:Internal Server Error";
                break;
            case 501:
                codeMessage = "501:Not Implemented";
                break;
            case 502:
                codeMessage = "502:Bad Gateway";
                break;
            case 503:
                codeMessage = "503:Service Unavailable";
                break;
            case 504:
                codeMessage = "504:Gateway Timeout";
                break;
            case 505:
                codeMessage = " 505:HTTP Version Not Supported";
                break;
            case 506:
                codeMessage = "506:Variant Also Negotiates";
                break;
            case 507:
                codeMessage = "507:Insufficient Storage";
                break;
            case 509:
                codeMessage = "509:Bandwidth Limit Exceeded";
                break;
            case 510:
                codeMessage = "510:Not Extended";
                break;
            case 600:
                codeMessage = "600:Unparseable Response Headers";
                break;
            default:
                codeMessage = "";
                break;
        }
        return codeMessage;
    }
}
