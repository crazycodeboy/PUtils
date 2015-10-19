package com.jph.simple;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jph.putils.HttpUtil;
import com.jph.putils.http.HttpRequest;
import com.jph.putils.http.callback.RequestCallBack;
import com.jph.putils.http.entity.HttpException;
import com.jph.putils.http.entity.ResponseInfo;
//import com.ksudi.lib.android.login.KLogin;

public class Test extends Activity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView= (TextView) findViewById(R.id.textView);
    }
    public void onClick(View view){
        HttpUtil httpUtil=new HttpUtil();
        httpUtil.send(new HttpRequest("http://192.168.1.159/ksudi-star-api/android/client/expressGroup/searchExpressGroup/1",HttpRequest.HttpMethod.GET, null), new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo info) {
                textView.setText("time:"+System.currentTimeMillis()+" responseContent:"+info.getResponseContent()+" cookieStr:"+info.getCookie());
            }

            @Override
            public void onFailure(HttpException error) {
                textView.setText("time:"+System.currentTimeMillis()+" responseContent:"+error.getResponseContent()+" cookieStr:"+error.getCookie()+" error:"+error.getErrorMsg());
            }
        });
//        KLogin.login(this,"526971370@qq.com", "123456", new RequestCallBack() {
//            @Override
//            public void onSuccess(ResponseInfo info) {
//                textView.setText("time:"+System.currentTimeMillis()+" responseContent:"+info.getResponseContent()+" cookieStr:"+info.getCookie());
//            }
//
//            @Override
//            public void onFailure(HttpException error) {
//                textView.setText("time:"+System.currentTimeMillis()+" responseContent:"+error.getResponseContent()+" cookieStr:"+error.getCookie()+" error:"+error.getErrorMsg());
//            }
//        });
    }
}
