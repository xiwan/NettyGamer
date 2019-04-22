package com.xiwan.NettyGamer.Job;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xiwan.NettyGamer.App;
import com.xiwan.NettyGamer.Enum.ActorMode;
import com.xiwan.NettyGamer.cache.SystemCache;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.RequestRoute;

@Component("RequestQueueJob")
public class RequestQueueJob extends CronJob {
  
  @Value("#{configProperties['RequestQueueJob.REQUEST_MAX_TIMEOUT']}")
  private int timeout;
  @Value("#{configProperties['RequestQueueJob.REQUEST_QUEUE_DELAY']}")
  private int delay;

  @PostConstruct
  @Override
  public void init() {
    this.REQUEST_MAX_TIMEOUT = timeout;
    this.REQUEST_QUEUE_DELAY = delay;      
  }
  
  @Override
  public void job() {
    BlockingQueue<RequestData> requestQueue = SystemCache.getRequestQueue();
    if (requestQueue.size() == 0) return;
    
    RequestData rd = requestQueue.poll();
    RequestRoute requestRoute = App.gameServer.GetRoute(rd.getActionType());
    if (requestRoute != null) {
      int priority = requestRoute.getPriority();
      ActorMode actorMode = requestRoute.getActorMode();
      Consumer<RequestData> action = requestRoute.getAction();
      action.accept(rd);
      
    }
  }


}
