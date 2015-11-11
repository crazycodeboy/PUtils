package com.jph.putils.http.entity;

import com.jph.putils.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数
 * Author: JPH
 * Date: 2015/11/11  8:59
 */
public class RequestParams {
    private Map<String, Object> params;
    private String stringEntity;
    public RequestParams() {
        params=new HashMap<>();
    }
    public void addBodyParameter(String key,Object value){
        params.put(key,value);
    }
    public void setStringEntity(String entity){
        params.clear();
        this.stringEntity=entity;
    }
    public String getFormParams(){
        return Utils.genFormData(params);
    }
    public String getJsonParams(){
        return  Utils.mapToJsonStr(params);
    }
    public String getStringEntity() {
        return stringEntity;
    }
}
