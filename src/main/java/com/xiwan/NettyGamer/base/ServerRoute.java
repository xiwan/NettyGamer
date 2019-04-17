package com.xiwan.NettyGamer.base;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import com.xiwan.NettyGamer.Enum.ActorMode;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.RequestRoute;

public class ServerRoute {

  private static ConcurrentMap<Integer, RequestRoute> routeMap = new ConcurrentHashMap<Integer, RequestRoute>();

  public static void RenewRoute(int type, int priority, Consumer<RequestData> action, ActorMode actorMode) {
    RequestRoute rr = new RequestRoute();
    rr.setPriority(priority);
    rr.setActorMode(actorMode);
    rr.setAction(action);
    routeMap.put(type, rr);
  }
  
  public static void RenewRoute(int type, int priority, Consumer<RequestData> action) {
    RenewRoute(type, priority, action, ActorMode.NONE);
  }

  public static RequestRoute GetRoute(int actionType) {
    return routeMap.get(actionType);
  }
}
