package com.jph.putils.util;

import com.jph.putils.http.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.Map;
import java.util.Set;

import static com.jph.putils.http.HttpRequest.*;
import static com.jph.putils.http.HttpRequest.HttpMethod.POST;

/**
 * 工具类
 * Author: JPH
 * Date: 2015/10/12 0012 13:50
 */
public class Utils {
    /**
     * 生成要上传的数据(Form格式)
     * @param params
     * @return
     */
    public static String genFormData(Map<String, Object> params){
        if (params==null)return "";
        StringBuffer paramsData=new StringBuffer();
        Set<String> keys=params.keySet();
        for(String key:keys){
            paramsData.append(key)
                    .append("=")
                    .append(params.get(key))
                    .append("&");
        }
        paramsData.deleteCharAt(paramsData.length()-1);
        return paramsData.toString();
    }

    public static String genUrlWithParam(Map<String, Object> params,String originalUrl){
        StringBuilder url=new StringBuilder(originalUrl);
        url.append("?");
        return url.append(genFormData(params)).toString();
    }
    /**
     * 将Map转换成json string
     * @param params
     * @return
     */
    public static String mapToJsonStr(Map<String ,Object > params){
        if (params==null)return "";
        StringBuffer paramsData=new StringBuffer("{");
        Set<String> keys=params.keySet();
        for(String key:keys){
            paramsData.append("\"")
                    .append(key)
                    .append("\"")
                    .append(":")
                    .append("\"")
                    .append(params.get(key))
                    .append("\"")
                    .append(",");
        }
        paramsData.deleteCharAt(paramsData.length()-1);
        paramsData.append("}");
        return paramsData.toString();
    }
    /**
     * 根据流返回一个字符串信息
     * @param is
     * @return
     * @throws IOException
     */
    public static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        is.close();
        String data = bos.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        bos.close();
        return data;
    }

    /**
     * 判断HttpMethod是否支持上传数据
     * @param method
     * @return
     */
    public static boolean isWithData(HttpMethod method){
        switch (method){
            case GET:
            case DELETE:
                return false;
            case POST:
            case PUT:
                return true;
        }
        return true;
    }
}
