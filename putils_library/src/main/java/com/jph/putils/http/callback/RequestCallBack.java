package com.jph.putils.http.callback;

import com.jph.putils.exception.HttpException;
import com.jph.putils.http.entity.ResponseInfo;

/**
 * Author: JPH
 * Date: 2015/10/14 0014 10:32
 */
public interface RequestCallBack {
    void onSuccess(ResponseInfo info);
    void onFailure(HttpException error);
}