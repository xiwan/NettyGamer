package com.xiwan.NettyGamer.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.xiwan.NettyGamer.Job.IJob;

public class FixedRateTimer {

  private ScheduledExecutorService scheduledExecutorService;
  private long initialDelay = 1000l;
  private long period = 1000l;

  public FixedRateTimer(String name, long initialDelay, long period) {
    scheduledExecutorService = Executors.newScheduledThreadPool(1, new CustomThreadFactory(name));
    this.initialDelay = initialDelay;
    this.period = period;
  }

  public void execute(IJob cronJob) {
    Runnable task = new Runnable() {
      @Override
      public void run() {
        cronJob.job();
      }
    };

    scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
  }

}
