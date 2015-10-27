package com.jph.putils.http.entity;

/**
 * Author: JPH
 * Date: 2015/10/27 0027 10:00
 */
public interface DownLoadAction {
    void onStart();
    void onPause();
    void onDelete();
    void onReset();
}
