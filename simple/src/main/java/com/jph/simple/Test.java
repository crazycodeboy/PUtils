package com.jph.simple;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.jph.putils.HttpUtils;
import com.jph.putils.exception.HttpException;
import com.jph.putils.http.HttpConfig;
import com.jph.putils.http.HttpRequest;
import com.jph.putils.http.callback.RequestCallBack;
import com.jph.putils.http.entity.BaseResponseInfo;
import com.jph.putils.http.entity.ResponseInfo;

public class Test extends Activity {

    private TextView textView;
    private String cookie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView= (TextView) findViewById(R.id.test);
    }
    public void access(View view){
        HttpUtils HttpUtils=new HttpUtils();
        HttpUtils.send(new HttpRequest("http://192.168.1.18:1061/api/android/client/expressGroup/searchExpressGroup/1",HttpRequest.HttpMethod.GET, null,new HttpConfig().setCookie(cookie)), new RequestCallBack() {
            @Override
            public void onStart() {
                textView.setText("onStart");
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                textView.setText("onLoading:total:"+total+" current:"+current);
            }

            @Override
            public void onSuccess(ResponseInfo info) {
                textView.setText("time:"+System.currentTimeMillis()+" responseContent:"+info.getResponseContent()+" cookieStr:"+info.getCookie());
            }

            @Override
            public void onFailure(HttpException error) {
                BaseResponseInfo info=error.getResponseInfo();
                textView.setText("time:"+System.currentTimeMillis()+" responseContent:"+info.getResponseContent()+" cookieStr:"+info.getCookie()+" error:"+error.getErrorMsg());
            }
        });
    }

}
