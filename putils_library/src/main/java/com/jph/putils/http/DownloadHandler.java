package com.jph.putils.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jph.putils.exception.HttpException;
import com.jph.putils.http.callback.RequestCallBack1;
import com.jph.putils.http.download.DownloadHttpTool;
import com.jph.putils.http.entity.DownLoadAction;

/**
 * 将下载方法封装在此类 提供下载，暂停，删除，以及重置的方法
 */
public class DownloadHandler implements DownLoadAction{
	/** * 下载失败	 */
	public final static int WHAT_ERROR=-1;
	/** * 更新下载进度	 */
	public final static int WHAT_UPDATE=1;
	private DownloadHttpTool mDownloadHttpTool;
	private long fileSize;
	private long downloadedSize = 0;
	private RequestCallBack1 requestCallBack;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case WHAT_UPDATE://更新下载进度
					int length = msg.arg1;
					synchronized (this) {// 加锁保证已下载的正确性
						downloadedSize += length;
					}
					if (requestCallBack != null) {
						requestCallBack.onLoading(fileSize, downloadedSize, false);
					}
					if (downloadedSize >= fileSize) {
						mDownloadHttpTool.compelete();
						if (requestCallBack != null) {
							requestCallBack.onSuccess(null);
						}
					}
					break;
				case WHAT_ERROR://下载失败
					if (requestCallBack != null) {
						requestCallBack.onFailure((HttpException) msg.obj);
					}
					break;
			}
		}

	};

	public DownloadHandler(int threadCount, String filePath, String filename,
						   String urlString, Context context) {

		mDownloadHttpTool = new DownloadHttpTool(threadCount, urlString,
				filePath, filename, context, mHandler);
	}

	// 下载之前首先异步线程调用ready方法获得文件大小信息，之后调用开始方法
	@Override
	public void onStart() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				mDownloadHttpTool.ready();
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				fileSize = mDownloadHttpTool.getFileSize();
				downloadedSize = mDownloadHttpTool.getCompeleteSize();
				Log.w("Tag", "downloadedSize::" + downloadedSize);
				if (downloadedSize>=fileSize){//下载完成
					requestCallBack.onSuccess(null);
					mDownloadHttpTool.compelete();
				}else {
					requestCallBack.onStart();
					mDownloadHttpTool.onStart();
				}
			}
		}.execute();
	}
	@Override
	public void onPause() {
		mDownloadHttpTool.onPause();
	}
	@Override
	public void onDelete() {
		mDownloadHttpTool.onDelete();
	}
	@Override
	public void onReset() {
		mDownloadHttpTool.onReset();
		onStart();
	}

	public void setOnDownloadListener(RequestCallBack1 requestCallBack) {
		this.requestCallBack = requestCallBack;
	}
}