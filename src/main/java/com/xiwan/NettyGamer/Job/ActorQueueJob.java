package com.xiwan.NettyGamer.Job;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import com.xiwan.NettyGamer.Enum.ActorMode;
import com.xiwan.NettyGamer.base.GameServer;
import com.xiwan.NettyGamer.base.ServerRoute;
import com.xiwan.NettyGamer.cache.ActorCache;
import com.xiwan.NettyGamer.entity.Actor;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.RequestRoute;

public class ActorQueueJob extends CronJob {

  private static ActorQueueJob instance = new ActorQueueJob();

  private ActorQueueJob() {
    super.REQUEST_MAX_TIMEOUT = 50;
    super.REQUEST_QUEUE_DELAY = 100;
  }

  public static ActorQueueJob Instance() {
    return instance;
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

      Future currentTask = ActorCache.fixedThreadExecutor.submit(new Runnable() {
        @Override
        public void run() {
          // TODO Auto-generated method stub
          BlockingQueue<RequestData> requestQueue = actor.getRequestQueue();
          if (requestQueue.size() == 0)
            return;
          
          while(requestQueue.peek() != null) {
            RequestData rd = requestQueue.poll();
            RequestRoute requestRoute = GameServer.Instance().GetRoute(rd.getActionType());
            if (requestRoute != null) {
              int priority = requestRoute.getPriority();
              ActorMode actorMode = requestRoute.getActorMode();
              Consumer<RequestData> action = requestRoute.getAction();

              action.accept(rd);
            }
          }

        }
      });

      actor.setCurrentTask(currentTask);

    }

  }

}
