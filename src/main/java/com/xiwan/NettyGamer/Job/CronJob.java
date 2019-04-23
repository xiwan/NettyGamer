package com.xiwan.NettyGamer.Job;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import com.xiwan.NettyGamer.utils.FixedRateTimer;
import com.xiwan.NettyGamer.utils.LogHelper;

public abstract class CronJob implements IJob, Runnable {

  protected String JobName = "CronJob-";
  public int REQUEST_MAX_TIMEOUT = 1000;
  public int REQUEST_QUEUE_DELAY = 1000;

  protected FixedRateTimer timer;
  protected Consumer consumerTask;

  private void register() {
    this.JobName = this.JobName + this.getClass().getSimpleName();
    this.consumerTask = (rd) -> this.job();
  }

  public void start() {
    register();
    
    this.timer = new FixedRateTimer(this.JobName, this.REQUEST_MAX_TIMEOUT, this.REQUEST_QUEUE_DELAY);
    this.timer.setCurrentTask(this.timer.execute(this));
    LogHelper.WriteDebugLog(String.format("START JOB[%s] timeout[%d] delay[%d] result[%s]", this.JobName,
        this.REQUEST_MAX_TIMEOUT, this.REQUEST_QUEUE_DELAY, this.timer.getCurrentTask() != null));
  }

  public void stop() {
    String log = String.format("STOP JOB[%s] timeout[%d] delay[%d]", this.JobName,
        this.REQUEST_MAX_TIMEOUT, this.REQUEST_QUEUE_DELAY);
    LogHelper.WriteDebugLog(log);
    
    shutdown();
    Future future = this.timer.getCurrentTask();
    if (future != null && (future.isCancelled() || future.isCancelled())) {
      this.timer.setCurrentTask(null);
    }
    
    if (future != null) {
      future.cancel(false);
      this.timer.setCurrentTask(null);
    }
    ScheduledExecutorService ex = this.timer.getScheduledExecutorService();
    List<Runnable> remainList = ex.shutdownNow();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (remainList == null || (ex.isShutdown() && ex.isTerminated())) {
      //System.out.println(log);
    }
  }
  

  @Override
  public void run() {
    // TODO Auto-generated method stub
    this.consumerTask.accept(null);
  }

}
