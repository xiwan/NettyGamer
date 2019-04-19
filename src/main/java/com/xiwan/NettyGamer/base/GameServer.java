package com.xiwan.NettyGamer.base;

import java.util.function.Consumer;

import com.xiwan.NettyGamer.Enum.ActorMode;
import com.xiwan.NettyGamer.Enum.ServerResult;
import com.xiwan.NettyGamer.Job.ActorQueueJob;
import com.xiwan.NettyGamer.Job.RequestQueueJob;
import com.xiwan.NettyGamer.cache.ActorCache;
import com.xiwan.NettyGamer.cache.SystemCache;
import com.xiwan.NettyGamer.controller.none.TestController;
import com.xiwan.NettyGamer.entity.Actor;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.RequestRoute;
import com.xiwan.NettyGamer.entity.ResponseData;
import com.xiwan.NettyGamer.entity.ResponseData.ResponseDataBuilder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class GameServer extends ServerBase {

  private static GameServer instance = new GameServer();
  
  private ServerRoute routeTable = ServerRoute.Instance();

  private GameServer() {};

  public static GameServer Instance() {
    return instance;
  }

  public RequestRoute GetRoute(int actionType) {
    return routeTable.GetRoute(actionType);
  }
  
  @Override
  public void StartServer() {
    Start();
  }

  @Override
  public void ShutdownServer() {
    Shutdown();
  }

  @Override
  public void LocalizeRequestRouteTable() {
    TestController TestController = new TestController();
    routeTable.RenewRoute(0x0010, 0, (rd) -> TestController.Login(rd));
    routeTable.RenewRoute(0xEE0000, 0, (rd) -> TestController.Profile(rd), ActorMode.GAMER);
  }

  @Override
  public void StartTimer() {
    RequestQueueJob.Instance().run();
    ActorQueueJob.Instance().run();
  }

  @Override
  public boolean ActorAction(RequestData rd, ActorMode actorMode) {
    switch (actorMode)
      {
      case PASSPORT:
        break;
      case GAMER:
        String uniqueID = rd.getRawData().getVarTable(0);
        Actor actor = ActorCache.getActor(uniqueID);
        if (actor != null) {
          return actor.PushRequest(rd);
        }
        break;
      case RAID:
        break;
      case NONE:
      default:
        return SystemCache.PushRequest(rd);
      }
    return true;
  }

  @Override
  public void ReceiveData(RequestData rd) {
    RequestRoute requestRoute = ServerRoute.Instance().GetRoute(rd.getActionType());
    if (requestRoute != null) {
      int priority = requestRoute.getPriority();
      Consumer<RequestData> action = requestRoute.getAction();
      ActorMode actorMode = requestRoute.getActorMode();

      boolean isSuccess = this.ActorAction(rd, actorMode);
      if (!isSuccess) {
        ResponseDataBuilder dataBuilder = ResponseData.builder().Type(ServerResult.ServerError.getCode())
            .Ticket(rd.getTicket());

        SendData(rd.getSocketContext(), dataBuilder.build());
      }
    }
  }

  @Override
  public void SendData(ChannelHandlerContext ctx, ResponseData data) {
    // TODO Auto-generated method stub
    if (ctx == null)
      return;
    byte[] out = data.toByteArray();
    ByteBuf buf = Unpooled.buffer(out.length, out.length);
    buf.writeBytes(out);
    ctx.writeAndFlush(buf);
  }

}
