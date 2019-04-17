package com.xiwan.NettyGamer.base;

import java.util.function.Consumer;

import com.xiwan.NettyGamer.Client.ActorCache;
import com.xiwan.NettyGamer.Enum.ActorMode;
import com.xiwan.NettyGamer.Server.FramedServer;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.RequestRoute;

import io.netty.channel.ChannelHandlerContext;

public abstract class ServerBase {

  public String version;
  public int port;
  private int maxPackageSize;
  private FramedServer framedServer;

  public void Initialize(String configPath) {
    this.port = 8010;
    this.maxPackageSize = 4096;
    this.LocalizeRequestRouteTable();
  }

  public abstract void StartServer();

  public abstract void ShutdownServer();

  public abstract void StartTimer();

  public abstract void LocalizeRequestRouteTable();
  
  public abstract boolean ActorAction(RequestData rd, ActorMode actorMode);

  protected void Start() {

    try {
      framedServer = new FramedServer(this.port, this.maxPackageSize, (rd) -> ReceiveData(rd),
          (ctx) -> Disconnect(ctx));
      if (framedServer != null)
        framedServer.run();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  protected void Shutdown() {
    try {
      if (framedServer != null)
        framedServer.stop();
      
      ActorCache.fixedThreadExecutor.shutdown();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void ReceiveData(RequestData rd) {
    RequestRoute requestRoute = ServerRoute.GetRoute(rd.getActionType());
    if (requestRoute != null) {
      int priority = requestRoute.getPriority();
      Consumer<RequestData> action = requestRoute.getAction();
      ActorMode actorMode = requestRoute.getActorMode();
      
      this.ActorAction(rd, actorMode);
    }
  }

  private static void Disconnect(ChannelHandlerContext ctx) {

  }
}
