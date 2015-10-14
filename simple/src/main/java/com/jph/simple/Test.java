package com.jph.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.jph.putils.HttpUtil;
import com.jph.putils.http.HttpRequest;
import com.jph.putils.http.callback.RequestCallBack;

public class Test extends AppCompatActivity {

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
            public void onSuccess(String responseContent, String cookieStr) {
                textView.setText("time:"+System.currentTimeMillis()+" responseContent:"+responseContent+" cookieStr:"+cookieStr);
            }
            @Override
            public void onFailure(String error) {
                textView.setText("time:"+System.currentTimeMillis()+"error:"+error);
            }
        });
    }
}
