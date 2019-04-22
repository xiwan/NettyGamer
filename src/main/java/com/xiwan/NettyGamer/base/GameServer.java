package com.xiwan.NettyGamer.base;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiwan.NettyGamer.Enum.ActorMode;
import com.xiwan.NettyGamer.Enum.ServerResult;
import com.xiwan.NettyGamer.Job.ActorQueueJob;
import com.xiwan.NettyGamer.Job.LogJob;
import com.xiwan.NettyGamer.Job.RequestQueueJob;
import com.xiwan.NettyGamer.cache.Actor;
import com.xiwan.NettyGamer.cache.ActorCache;
import com.xiwan.NettyGamer.cache.SystemCache;
import com.xiwan.NettyGamer.controller.none.TestController;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.RequestRoute;
import com.xiwan.NettyGamer.entity.ResponseData;
import com.xiwan.NettyGamer.entity.ResponseData.ResponseDataBuilder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

@Component("GameServer")
public class GameServer extends ServerBase {

  public Boolean isRunning = false;
  @Autowired
  private ServerRoute routeTable;
  @Autowired
  private TestController testController;
  @Autowired
  private LogJob logJob;
  @Autowired
  private ActorQueueJob actorQueueJob;
  @Autowired
  private RequestQueueJob requestQueueJob;

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
    
    routeTable.RenewRoute(0x0010, 0, (rd) -> testController.Login(rd));
    routeTable.RenewRoute(0xEE0000, 0, (rd) -> testController.Profile(rd), ActorMode.GAMER);
  }

  @Override
  public void StartTimer() {
    requestQueueJob.run();
    actorQueueJob.run();
    logJob.run();
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
    RequestRoute requestRoute = routeTable.GetRoute(rd.getActionType());
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
    // System.out.println(ByteBufUtil.hexDump(buf));
    ctx.writeAndFlush(buf);
  }

}
