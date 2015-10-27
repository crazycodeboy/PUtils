package com.jph.putils.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.jph.putils.http.download.DownloadHttpTool.DownloadState;
import com.jph.putils.exception.HttpException;
import com.jph.putils.http.callback.RequestCallBack;
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
	/** * 要下载的文件发生了改变*/
	public final static int WHAT_CHANGE=0;
	private DownloadHttpTool mDownloadHttpTool;
	private long fileSize;
	private long downloadedSize = 0;
	private RequestCallBack requestCallBack;

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
				case WHAT_CHANGE://下载的文件发生了改变
					onReset();
					break;
			}
		}

	};
	public DownloadHandler(String url, String target,Context context,RequestCallBack callBack1) {
		this(url,target,2,context,callBack1);
	}
	public DownloadHandler(String url, String target,int threadCount, Context context,RequestCallBack callBack1) {
		if (threadCount>4)threadCount=4;
		this.requestCallBack=callBack1;
		mDownloadHttpTool = new DownloadHttpTool(url,target,threadCount,context,mHandler);
	}
	// 下载之前首先异步线程调用ready方法获得文件大小信息，之后调用开始方法
	@Override
	public void onStart() {
		new AsyncTask<Void, Void, DownloadState>() {
			@Override
			protected DownloadState doInBackground(Void... arg0) {
				return mDownloadHttpTool.ready();
			}
			@Override
			protected void onPostExecute(DownloadState state) {
				if (!DownloadState.Ready.equals(state))return;
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
}