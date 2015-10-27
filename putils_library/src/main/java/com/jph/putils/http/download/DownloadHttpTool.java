package com.jph.putils.http.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jph.putils.exception.HttpException;
import com.jph.putils.http.DownloadHandler;
import com.jph.putils.http.entity.BaseResponseInfo;
import com.jph.putils.http.entity.DownLoadAction;

/**
 * 
 * 利用Http协议进行多线程下载具体实践类
 */
public class DownloadHttpTool implements DownLoadAction{

	private static final String TAG = DownloadHttpTool.class.getSimpleName();
	// 线程数量
	private int threadCount;
	// URL地址
	private String urlstr;
	private Context mContext;
	private Handler mHandler;
	// 保存下载信息的类
	private List<DownloadInfo> downloadInfos;
	// 本地保存路径
	private String target;
	private int fileSize;
	// 文件信息保存的数据库操作类
	private DownlaodSqlTool sqlTool;
	//下载文件的最后一次修改时间
	private long lastModified;
	// 利用枚举表示下载的三种状态
	public enum DownloadState {
		Downloading, Pause, Ready, Delete,Fail;
	}

	// 当前下载状态
	private DownloadState state = DownloadState.Ready;
	// 所有线程下载的总数
	private int globalCompelete = 0;

	public DownloadHttpTool(String urlString,
			String target, int threadCount, Context context, Handler handler) {
		super();
		this.urlstr = urlString;
		this.target = target;
		this.threadCount = threadCount;
		this.mContext = context;
		this.mHandler = handler;
		sqlTool = new DownlaodSqlTool(mContext);
	}

	// 在开始下载之前需要调用ready方法进行配置
	public DownloadState ready() {
		if (state == DownloadState.Downloading) {
			return DownloadState.Downloading;
		}
		Log.w(TAG, "ready");
		globalCompelete = 0;
		downloadInfos = sqlTool.getInfos(urlstr);
		if (downloadInfos.size() == 0) {
			if (!initFirst())return DownloadState.Fail;
		} else {
			File file = new File(target);
			if (!file.exists()) {
				sqlTool.delete(urlstr);
				if (!initFirst())return DownloadState.Fail;
			} else {
				fileSize = downloadInfos.get(downloadInfos.size() - 1)
						.getEndPos();
				for (DownloadInfo info : downloadInfos) {
					globalCompelete += info.getCompeleteSize();
				}
				Log.w(TAG, "globalCompelete:::" + globalCompelete);
			}
		}
		return DownloadState.Ready;
	}
	@Override
	public void onStart() {
		Log.w(TAG, "onStart");
		if (downloadInfos != null) {
			if (state == DownloadState.Downloading) {
				return;
			}
			state = DownloadState.Downloading;
			for (DownloadInfo info : downloadInfos) {
				Log.v(TAG, "startThread");
				new DownloadThread(info).start();
			}
		}
	}
	@Override
	public void onPause() {
		state = DownloadState.Pause;
		sqlTool.closeDb();
	}
	@Override
	public void onDelete() {
		state = DownloadState.Delete;
		compelete();
		new File(target).delete();
	}
	@Override
	public void onReset() {
		onDelete();
	}
	public void compelete() {
		sqlTool.delete(urlstr);
		sqlTool.closeDb();
	}

	public int getFileSize() {
		return fileSize;
	}

	public int getCompeleteSize() {
		return globalCompelete;
	}

	/**
	 * 第一次下载初始化
	 */
	private boolean initFirst() {
		Log.w(TAG, "initFirst");
		HttpURLConnection connection=null;
		try {
			URL url = new URL(urlstr);
			connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			int httpCode=connection.getResponseCode();
			fileSize = connection.getContentLength();
			lastModified=connection.getLastModified();
			Log.w(TAG, "fileSize::" + fileSize);
			String fileParentPath=new File(target).getParent();
			File fileParent = new File(fileParentPath);
			if (!fileParent.exists()) {
				fileParent.mkdir();
			}
			File file = new File(target);
			if (!file.exists()) {
				file.createNewFile();
			}
			// 本地访问文件
			RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
			if (fileSize>0)accessFile.setLength(fileSize);
			accessFile.close();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			sendDownLoadErrorMessage(null,e.toString());
			return false;
		}finally {
			if (connection!=null)connection.disconnect();
		}
		int range = fileSize / threadCount;
		downloadInfos = new ArrayList<DownloadInfo>();
		for (int i = 0; i < threadCount - 1; i++) {
			DownloadInfo info = new DownloadInfo(i, i * range, (i + 1) * range
					- 1, 0, urlstr,lastModified);
			downloadInfos.add(info);
		}
		DownloadInfo info = new DownloadInfo(threadCount - 1, (threadCount - 1)
				* range, fileSize - 1, 0, urlstr,lastModified);
		downloadInfos.add(info);
		sqlTool.insertInfos(downloadInfos);
		return true;
	}

	/**
	 * 自定义下载线程
	 * 
	 * @author zhaokaiqiang
	 * @time 2015-2-25下午5:52:28
	 */
	private class DownloadThread extends Thread {

		private int threadId;
		private int startPos;
		private int endPos;
		private int compeleteSize;
		private String urlstr;
		private int totalThreadSize;
		private DownloadInfo downloadInfo;
		public DownloadThread(DownloadInfo downloadInfo) {
			this.downloadInfo=downloadInfo;
			this.threadId = downloadInfo.getThreadId();
			this.startPos = downloadInfo.getStartPos();
			this.endPos = downloadInfo.getEndPos();
			totalThreadSize = endPos - startPos + 1;
			this.urlstr = downloadInfo.getUrl();
			this.compeleteSize = downloadInfo.getCompeleteSize();
		}

		@Override
		public void run() {
			HttpURLConnection connection = null;
			RandomAccessFile randomAccessFile = null;
			InputStream is = null;
			try {
				randomAccessFile = new RandomAccessFile(target, "rwd");
				randomAccessFile.seek(startPos + compeleteSize);
				URL url = new URL(urlstr);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Range", "bytes="
						+ (startPos + compeleteSize) + "-" + endPos);
				Log.i(TAG,"Range ：bytes="+ (startPos + compeleteSize) + "-" + endPos+" total::" + totalThreadSize);
				if(downloadInfo.getLastModified()!=connection.getLastModified()){//下载的文件发生了改变
					mHandler.sendEmptyMessage(DownloadHandler.WHAT_CHANGE);
					return;
				}
				is = connection.getInputStream();
				byte[] buffer = new byte[1024];
				int length = -1;
				while ((length = is.read(buffer)) != -1) {
					randomAccessFile.write(buffer, 0, length);
					compeleteSize += length;
					sendUpdateMessage(length);
					Log.w(TAG, "Threadid::" + threadId + "    compelete::"
							+ compeleteSize + "    total::" + totalThreadSize);
					// 当程序不再是下载状态的时候，纪录当前的下载进度
					if ((state != DownloadState.Downloading)
							|| (compeleteSize >= totalThreadSize)) {
						sqlTool.updataInfos(threadId, compeleteSize, urlstr);
						break;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				sendDownLoadErrorMessage(null,downloadInfo, "threadId::" + threadId + e.toString());
				Log.e(TAG, "threadId::" + threadId + e.toString());
				sqlTool.updataInfos(threadId, compeleteSize, urlstr);
			} finally {
				try {
					if (is != null) {
						is.close();
					}
					randomAccessFile.close();
					if (connection!=null)connection.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
					sendDownLoadErrorMessage(null, e.toString());
				}
			}
		}
	}

	/**
	 * 发送下载失败的消息
	 * @param errorMsg
	 */
	private void sendDownLoadErrorMessage(BaseResponseInfo info,String errorMsg){

		HttpException error=new HttpException(info,errorMsg);
		Message msg=new Message();
		msg.obj=error;
		msg.what= DownloadHandler.WHAT_ERROR;
		mHandler.sendMessage(msg);
		onPause();
	}
	/**
	 * 发送下载失败的消息
	 * @param downloadInfo 出错的线程要现在的信息描述
	 * @param errorMsg 出错消息
	 */
	private void sendDownLoadErrorMessage(BaseResponseInfo info,DownloadInfo downloadInfo,String errorMsg){
		downloadInfo.setIsError(true);
		for (DownloadInfo temp:downloadInfos){//如果所有的下载线程都出错，则发送下载出错消息
			if (!temp.isError())return;
		}
		sendDownLoadErrorMessage(info,errorMsg);
	}
	/**
	 * 发送更新下载进度的消息
	 * @param length
	 */
	private void sendUpdateMessage(int length){
		Message message = Message.obtain();
		message.what =DownloadHandler.WHAT_UPDATE;
		message.obj = urlstr;
		message.arg1 = length;
		mHandler.sendMessage(message);
	}
}