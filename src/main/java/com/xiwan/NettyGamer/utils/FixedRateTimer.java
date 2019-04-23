package com.xiwan.NettyGamer.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.xiwan.NettyGamer.Job.CronJob;
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

  public Future execute(CustomJob job) {
    return scheduledExecutorService.scheduleAtFixedRate(job, initialDelay, period, TimeUnit.MILLISECONDS);
  }
  
  public Boolean cancel(Future future) {
    if (future != null && (future.isCancelled() || future.isCancelled())) {
      return true;
    }
    return future.cancel(false);
  }

}
