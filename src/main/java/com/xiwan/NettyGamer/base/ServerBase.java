package com.xiwan.NettyGamer.base;

import com.xiwan.NettyGamer.Enum.ActorMode;
import com.xiwan.NettyGamer.Server.FramedServer;
import com.xiwan.NettyGamer.cache.ActorCache;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.ResponseData;
import com.xiwan.NettyGamer.proto.ServerResponse;

import io.netty.channel.ChannelHandlerContext;

public abstract class ServerBase {

  public String version;
  public int port;
  protected int maxPackageSize;
  protected int maxReaderIdleTime = 0;
  protected int maxWriterIdleTime = 0;
  protected int maxAllIdleTime = 0;
  private FramedServer framedServer;

  public void Initialize(String configPath) {
    this.port = 8080;
    this.maxPackageSize = 4096;
    this.maxReaderIdleTime = 120;
    this.LocalizeRequestRouteTable();
  }

  protected void Start() {
    try {
      framedServer = new FramedServer(this.port, this.maxPackageSize, this.maxReaderIdleTime, this.maxWriterIdleTime,
          this.maxAllIdleTime, (rd) -> ReceiveData(rd), (ctx) -> Disconnect(ctx));
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

  private static void Disconnect(ChannelHandlerContext ctx) {

  }

  public abstract void StartServer();

  public abstract void ShutdownServer();

  public abstract void StartTimer();

  public abstract void LocalizeRequestRouteTable();
  
  public abstract void ReceiveData(RequestData rd);
  
  public abstract void SendData(ChannelHandlerContext ctx, ResponseData data);

  public abstract boolean ActorAction(RequestData rd, ActorMode actorMode);
}
