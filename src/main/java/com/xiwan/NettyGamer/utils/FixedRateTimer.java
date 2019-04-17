package com.xiwan.NettyGamer.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.xiwan.NettyGamer.Job.IJob;

public class FixedRateTimer {

  private ScheduledExecutorService scheduledExecutorService;
  private long initialDelay = 100l;
  private long period = 100l;

  public FixedRateTimer(long initialDelay, long period) {
    this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
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
