package com.xiwan.NettyGamer.Job;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xiwan.NettyGamer.utils.LogHelper;

@Component("LogJob")
public class LogJob extends CronJob {
  
  @Value("#{configProperties['LogJob.REQUEST_MAX_TIMEOUT']}")
  private int timeout;
  @Value("#{configProperties['LogJob.REQUEST_QUEUE_DELAY']}")
  private int delay;

  @PostConstruct
  @Override
  public void init() {
    this.REQUEST_MAX_TIMEOUT = timeout;
    this.REQUEST_QUEUE_DELAY = delay;      
  }
  
  @Override
  public void job() {
    // TODO Auto-generated method stub
    LogHelper.SaveLog();
  }

}
