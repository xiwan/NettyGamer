package com.xiwan.NettyGamer.Job;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

import com.xiwan.NettyGamer.Client.SystemCache;
import com.xiwan.NettyGamer.Enum.ActorMode;
import com.xiwan.NettyGamer.base.GameServer;
import com.xiwan.NettyGamer.base.ServerRoute;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.RequestRoute;
import com.xiwan.NettyGamer.utils.FixedRateTimer;

public class RequestQueueJob extends CronJob {
  
  private static RequestQueueJob instance = new RequestQueueJob();
  
  private RequestQueueJob() {
    super.REQUEST_MAX_TIMEOUT = 50;
    super.REQUEST_QUEUE_DELAY = 100;
  }
  
  public static RequestQueueJob Instance() {
    return instance;
  }
  
  @Override
  public void job() {
    BlockingQueue<RequestData> requestQueue = SystemCache.getRequestQueue();
    if (requestQueue.size() == 0) return;
    
    RequestData rd = requestQueue.poll();
    RequestRoute requestRoute = ServerRoute.GetRoute(rd.getActionType());
    if (requestRoute != null) {
      int priority = requestRoute.getPriority();
      ActorMode actorMode = requestRoute.getActorMode();
      Consumer<RequestData> action = requestRoute.getAction();
      
      action.accept(rd);
    }
  }

}
