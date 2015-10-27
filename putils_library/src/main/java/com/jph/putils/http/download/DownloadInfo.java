package com.jph.putils.http.download;

public class DownloadInfo {
	/**下载线程id**/
	private int threadId;
	/**开始点**/
	private int startPos;
	/**结束点**/
	private int endPos;
	/**完成度**/
	private int compeleteSize;
	/**下载文件的URL地址**/
	private String url;
	/**当前下载线程是否出错**/
	private boolean isError;
	/**下载文件的最后一次修改时间**/
	private long lastModified;
	public DownloadInfo(int threadId, int startPos, int endPos,
			int compeleteSize, String url,long lastModified) {
		this.threadId = threadId;
		this.startPos = startPos;
		this.endPos = endPos;
		this.compeleteSize = compeleteSize;
		this.url = url;
		this.lastModified=lastModified;
	}

	public DownloadInfo() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public int getStartPos() {
		return startPos;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	public int getEndPos() {
		return endPos;
	}

	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}

	public int getCompeleteSize() {
		return compeleteSize;
	}

	public void setCompeleteSize(int compeleteSize) {
		this.compeleteSize = compeleteSize;
	}

	public boolean isError() {
		return isError;
	}

	public void setIsError(boolean isError) {
		this.isError = isError;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		return "DownloadInfo [threadId=" + threadId + ", startPos=" + startPos
				+ ", endPos=" + endPos + ", compeleteSize=" + compeleteSize
				+ "]";
	}
}
