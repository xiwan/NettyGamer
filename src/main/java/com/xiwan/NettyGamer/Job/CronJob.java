package com.xiwan.NettyGamer.Job;

import com.xiwan.NettyGamer.utils.FixedRateTimer;

public abstract class CronJob implements IJob {

  protected String JobName = "CronJob";
  protected int REQUEST_MAX_TIMEOUT = 1000;
  protected int REQUEST_QUEUE_DELAY = 1000;
  
  public void run() {
    FixedRateTimer fixedRateTimer = new FixedRateTimer(REQUEST_MAX_TIMEOUT, REQUEST_QUEUE_DELAY);
    fixedRateTimer.execute(this);
  }
  
}
