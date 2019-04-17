package com.xiwan.NettyGamer.Client;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xiwan.NettyGamer.entity.Actor;

public class ActorCache {
  
  private static ConcurrentMap <String, Actor> actorMap = new ConcurrentHashMap<String, Actor>();
  
  private final static int CONCURRENT_ACTOR_NUMBER = 8;
  public final static ExecutorService fixedThreadExecutor = Executors.newFixedThreadPool(CONCURRENT_ACTOR_NUMBER);
  
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

}
