package com.xiwan.NettyGamer.Job;

import com.xiwan.NettyGamer.utils.LogHelper;

public class LogJob extends CronJob {

  private static LogJob instance = new LogJob();
  
  private LogJob() {
    this.REQUEST_MAX_TIMEOUT = 50;
    this.REQUEST_QUEUE_DELAY = 1000;
  }
  
  public static LogJob Instance() {
    return instance;
  }
  
  @Override
  public void job() {
    // TODO Auto-generated method stub
    LogHelper.SaveLog();
  }

}
