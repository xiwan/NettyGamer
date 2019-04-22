package com.xiwan.NettyGamer.base;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.xiwan.NettyGamer.Enum.ActorMode;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.RequestRoute;

@Component("ServerRoute")
public class ServerRoute {
  private ConcurrentMap<Integer, RequestRoute> routeMap = new ConcurrentHashMap<Integer, RequestRoute>();
  private static ServerRoute instance = new ServerRoute();
  
  private ServerRoute() {}
  public static ServerRoute Instance() {
    return instance;
  }

  public void RenewRoute(int type, int priority, Consumer<RequestData> action, ActorMode actorMode) {
    RequestRoute rr = new RequestRoute();
    rr.setPriority(priority);
    rr.setActorMode(actorMode);
    rr.setAction(action);
    routeMap.put(type, rr);
  }
  
  public void RenewRoute(int type, int priority, Consumer<RequestData> action) {
    RenewRoute(type, priority, action, ActorMode.NONE);
  }

  public RequestRoute GetRoute(int actionType) {
    return routeMap.get(actionType);
  }
}
