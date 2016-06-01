package com.whh.watcher.receiver;


import com.whh.watcher.spi.MessageQueue;

public interface MessageAnalyzer {

	public void analyze(MessageQueue queue);

	public void destroy();

	public void doCheckpoint(boolean atEnd);

	public long getStartTime();

	public void initialize(long startTime, long duration, long extraTime);

	public int getAnanlyzerCount();

	public void setIndex(int index);

//	public ReportManager<?> getReportManager();
}
