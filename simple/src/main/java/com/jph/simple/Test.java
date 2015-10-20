package com.jph.simple;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.jph.putils.HttpUtil;
import com.jph.putils.http.HttpConfig;
import com.jph.putils.http.HttpRequest;
import com.jph.putils.http.callback.RequestCallBack;
import com.jph.putils.http.entity.HttpException;
import com.jph.putils.http.entity.ResponseInfo;
import com.ksudi.lib.android.login.KLogin;

public class Test extends Activity {

    private TextView textView;
    private String cookie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView= (TextView) findViewById(R.id.test);
    }
    public void login(View view){
        KLogin.login(this,"526971370@qq.com", "123456", new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo info) {
                textView.setText("time:"+System.currentTimeMillis()+" responseContent:"+info.getResponseContent()+" cookieStr:"+info.getCookie());
                cookie=info.getCookie();
            }

            @Override
            public void onFailure(HttpException error) {
                textView.setText("time:"+System.currentTimeMillis()+" responseContent:"+error.getResponseContent()+" cookieStr:"+error.getCookie()+" error:"+error.getErrorMsg());
            }
        });
    }
    public void access(View view){
        HttpUtil httpUtil=new HttpUtil();
        httpUtil.send(new HttpRequest("http://192.168.1.18:1061/api/android/client/expressGroup/searchExpressGroup/1",HttpRequest.HttpMethod.GET, null,new HttpConfig().setCookie(cookie)), new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo info) {
                textView.setText("time:"+System.currentTimeMillis()+" responseContent:"+info.getResponseContent()+" cookieStr:"+info.getCookie());
            }

            @Override
            public void onFailure(HttpException error) {
                textView.setText("time:"+System.currentTimeMillis()+" responseContent:"+error.getResponseContent()+" cookieStr:"+error.getCookie()+" error:"+error.getErrorMsg());
            }
        });
    }

    public void exit(View view){
        KLogin.exit(new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo info) {
                textView.setText("注销成功："+info.getResponseContent());
            }

            @Override
            public void onFailure(HttpException error) {
                textView.setText("注销失败："+error.getErrorMsg());
            }
        });
    }
}
