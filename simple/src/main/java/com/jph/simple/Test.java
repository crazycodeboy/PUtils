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
import com.ksudi.lib.android.login.util.LoginConfig;

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
        KLogin.login(this,getLoginConfig(),"526971370@qq.com", "123456", new RequestCallBack() {
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

    /**
     * 获取登录Url配置
     * @return
     */
    public static LoginConfig getLoginConfig(){
        LoginConfig config=new LoginConfig();
        config.setLoginUrl("http://192.168.1.18:1054/cas/api1/login")
                .setLogoutUrl("http://192.168.1.18:1054/cas/api1/logout")
                .setTicketUrl("http://192.168.1.18:1061/api/android/client/test/login/1")
                .setUploadLogUrl("http://192.168.1.18:1054/cas/api1/uploadLog");
        return config;
    }
}
