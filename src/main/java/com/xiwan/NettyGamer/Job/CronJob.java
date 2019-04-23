package com.xiwan.NettyGamer.Job;

import java.util.concurrent.Future;
import java.util.function.Consumer;

import com.xiwan.NettyGamer.utils.CustomJob;
import com.xiwan.NettyGamer.utils.FixedRateTimer;
import com.xiwan.NettyGamer.utils.LogHelper;

public abstract class CronJob implements IJob {

  protected String JobName = "CronJob-";
  public int REQUEST_MAX_TIMEOUT = 1000;
  public int REQUEST_QUEUE_DELAY = 1000;

  public void register(Consumer consumerTask) {
    CustomJob job = new CustomJob();
    job.setName(JobName + this.getClass().getSimpleName());
    job.setConsumerTask(consumerTask);
    job.setDelay(this.REQUEST_MAX_TIMEOUT);
    job.setTimeout(this.REQUEST_QUEUE_DELAY);

    jobList.add(job);

  }

  public static void startAll() {
    for (CustomJob job : jobList) {
      FixedRateTimer fixedRateTimer = new FixedRateTimer(job.getName(), job.getTimeout(), job.getDelay());
      job.setCurrentTask(fixedRateTimer.execute(job));
      LogHelper.WriteDebugLog(String.format("START JOB[%s] timeout[%d] delay[%d] result[%s]", job.getName(),
          job.getTimeout(), job.getTimeout(), job.getCurrentTask() != null));
    }
  }

  public static void stopAll() {
    for (CustomJob job : jobList) {
      Future future = job.getCurrentTask();
      if (future != null && (future.isCancelled() || future.isCancelled())) {
        return;
      }
      Boolean result = future.cancel(false);
      LogHelper.WriteDebugLog(String.format("STOP JOB[%s] timeout[%d] delay[%d] result[%s]", job.getName(),
          job.getTimeout(), job.getTimeout(), result));
    }
  }

}
