package com.xiwan.NettyGamer.Job;

import com.xiwan.NettyGamer.utils.FixedRateTimer;
import com.xiwan.NettyGamer.utils.LogHelper;

public abstract class CronJob implements IJob {

  protected String JobName = "CronJob-";
  protected int REQUEST_MAX_TIMEOUT = 1000;
  protected int REQUEST_QUEUE_DELAY = 1000;

  public void run() {
    String jobName = JobName + this.getClass().getSimpleName();
    LogHelper.WriteInfoLog(String.format("JOB[%s] timeout[%d] delay[%d]", jobName, this.REQUEST_MAX_TIMEOUT, this.REQUEST_QUEUE_DELAY));
    FixedRateTimer fixedRateTimer = new FixedRateTimer(jobName, this.REQUEST_MAX_TIMEOUT,
        this.REQUEST_QUEUE_DELAY);
    fixedRateTimer.execute(this);
  }

}
