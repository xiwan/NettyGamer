package com.xiwan.NettyGamer.Job;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xiwan.NettyGamer.App;
import com.xiwan.NettyGamer.Enum.ActorMode;
import com.xiwan.NettyGamer.cache.Actor;
import com.xiwan.NettyGamer.cache.ActorCache;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.RequestRoute;

@Component("ActorQueueJob")
public class ActorQueueJob extends CronJob {
  
  @Value("#{configProperties['ActorQueueJob.REQUEST_MAX_TIMEOUT']}")
  private int timeout;
  @Value("#{configProperties['ActorQueueJob.REQUEST_QUEUE_DELAY']}")
  private int delay;
  
  @PostConstruct
  @Override
  public void init() {
    this.REQUEST_MAX_TIMEOUT = timeout;
    this.REQUEST_QUEUE_DELAY = delay;  
  }
  
  @Override
  public void job() {

    Collection<Actor> actorList = ActorCache.getActorList();
    for (Actor actor : actorList) {
      // reset currentTask
      if (actor.getCurrentTask() != null && (actor.getCurrentTask().isDone() || actor.getCurrentTask().isCancelled())) {
        actor.setCurrentTask(null);
      }

      if (actor.getCurrentTask() != null)
        continue;
      
      Callable<Boolean> Task = new Callable<Boolean>() {

        @Override
        public Boolean call() throws Exception {
          // TODO Auto-generated method stub
          BlockingQueue<RequestData> requestQueue = actor.getRequestQueue();
          if (requestQueue.size() == 0)
            return true;
          
          while(requestQueue.peek() != null) {
            RequestData rd = requestQueue.poll();
            RequestRoute requestRoute = App.gameServer.GetRoute(rd.getActionType());
            if (requestRoute != null) {
              int priority = requestRoute.getPriority();
              ActorMode actorMode = requestRoute.getActorMode();
              Consumer<RequestData> action = requestRoute.getAction();

              action.accept(rd);
            }
          }
          return true;
        }
      };
      Future<Boolean> currentTask = ActorCache.executeTask(Task);

      actor.setCurrentTask(currentTask);

    }

  }

}
