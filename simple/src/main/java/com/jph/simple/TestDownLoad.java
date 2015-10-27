package com.jph.simple;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jph.putils.HttpUtils;
import com.jph.putils.exception.HttpException;
import com.jph.putils.http.DownloadHandler;
import com.jph.putils.http.callback.RequestCallBack1;
import com.jph.putils.http.entity.BaseResponseInfo;
import com.jph.putils.http.entity.ResponseInfo;

public class TestDownLoad extends FragmentActivity {

	private static final String TAG = TestDownLoad.class.getSimpleName();

	private ProgressBar mProgressBar;
	private Button start;
	private Button pause;
	private Button delete;
	private Button reset;
	private TextView tvCurrent;
	private TextView tvMsg;
	private ImageView image;

	private int max;

	private DownloadHandler mDownloadUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_download);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		start = (Button) findViewById(R.id.button_start);
		pause = (Button) findViewById(R.id.button_pause);
		delete = (Button) findViewById(R.id.button_delete);
		reset = (Button) findViewById(R.id.button_reset);
		tvCurrent = (TextView) findViewById(R.id.textView_total);
		tvMsg = (TextView) findViewById(R.id.tvMsg);
		image = (ImageView) findViewById(R.id.image);

		String urlString = "http://pic.ksudi.com/Ksudi_parttime.apk";
		final String target = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ADownLoadTest/abc.apk";
		mDownloadUtil = new HttpUtils().download(urlString,target,this,new RequestCallBack1() {
			@Override
			public void onStart() {
				mProgressBar.setMax(100);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				int i= (int) (current*1.0/total*100);
				tvCurrent.setText(i + "%");
				mProgressBar.setProgress(i);
				StringBuffer sb=new StringBuffer();
				sb.append("下载中：");
				sb.append("total:").append(total);
				sb.append("current:").append(current);
				tvMsg.setText(sb.toString());
			}

			@Override
			public void onSuccess(ResponseInfo info) {
				StringBuffer sb=new StringBuffer();
				sb.append("下载完成：");
//				sb.append("httpCode:");
//				sb.append(info.getHttpCode());
//				sb.append("\nresponseContent:");
//				sb.append(info.getResponseContent());
				tvMsg.setText(sb.toString());
			}

			@Override
			public void onFailure(HttpException error) {
				BaseResponseInfo info=error.getResponseInfo();
				StringBuffer sb=new StringBuffer();
				sb.append("下载失败：");
//				sb.append("\nhttpCode:");
//				sb.append(info.getHttpCode());
//				sb.append("\nerror:");
				sb.append(error.getErrorMsg());
//				sb.append("\nresponseContent:");
//				sb.append(info.getResponseContent());
				tvMsg.setText(sb.toString());
			}
		});
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mDownloadUtil.onStart();
			}
		});
		pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mDownloadUtil.onPause();
			}
		});
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mDownloadUtil.onDelete();
				mProgressBar.setProgress(0);
				tvCurrent.setText("0%");
				image.setImageBitmap(null);
			}
		});
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mDownloadUtil.onReset();
				mProgressBar.setProgress(0);
				tvCurrent.setText("0%");
				image.setImageBitmap(null);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
