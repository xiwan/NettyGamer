package com.xiwan.NettyGamer.cache;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.xiwan.NettyGamer.utils.CustomThreadFactory;


public class ActorCache {

  private static ConcurrentMap<String, Actor> actorMap = new ConcurrentHashMap<String, Actor>();

  private final static int CONCURRENT_ACTOR_NUMBER = 2048;
  private final static ExecutorService fixedThreadExecutor;

  static {
    fixedThreadExecutor = Executors.newFixedThreadPool(CONCURRENT_ACTOR_NUMBER,
        new CustomThreadFactory(ActorCache.class.getSimpleName()));
  }

  public static Collection<Actor> getActorList() {
    return actorMap.values();
  }

  public static Actor getActor(String uniqueID) {
    return actorMap.get(uniqueID);
  }

  public static void addActor(String uniqueID) {
    if (!actorMap.containsKey(uniqueID)) {
      Actor actor = new Actor();
      actor.setUniqueID(uniqueID);
      actorMap.put(uniqueID, actor);
    }
  }

  public static void removeActor(String uniqueID) {
    actorMap.remove(uniqueID);
  }
  
  public static Future<Boolean> executeTask(Callable<Boolean> task) {
    return fixedThreadExecutor.submit(task);
  }
  
  public static void shutdown() {
    fixedThreadExecutor.shutdown();
  }

}
