package com.xiwan.NettyGamer.base;

import com.xiwan.NettyGamer.Client.ActorCache;
import com.xiwan.NettyGamer.Client.SystemCache;
import com.xiwan.NettyGamer.Enum.ActorMode;
import com.xiwan.NettyGamer.Job.ActorQueueJob;
import com.xiwan.NettyGamer.Job.RequestQueueJob;
import com.xiwan.NettyGamer.controller.none.TestController;
import com.xiwan.NettyGamer.entity.Actor;
import com.xiwan.NettyGamer.entity.RequestData;

public class GameServer extends ServerBase {

  private static GameServer instance = new GameServer();

  private GameServer() {
  };

  public static GameServer Instance() {
    return instance;
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
    ServerRoute.RenewRoute(0x0010, 0, (rd)->TestController.Login(rd));
    ServerRoute.RenewRoute(0xEE0000, 0, (rd)->TestController.Profile(rd), ActorMode.GAMER);
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
          actor.PushRequest(rd);
        }
        break;
      case RAID:
        break;
      case NONE:
      default:
        SystemCache.PushRequest(rd);
        break;
      }
    return true;
  }

}
