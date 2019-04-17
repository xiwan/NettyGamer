package com.xiwan.NettyGamer.controller.none;

import com.xiwan.NettyGamer.Client.ActorCache;
import com.xiwan.NettyGamer.entity.Actor;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.utils.Guid;

public class TestController {
  
  public static void Login(RequestData rd) {
    System.out.println("Login");
    System.out.println(rd);
    
    String uuid = rd.getRawData().getVarTable(0);
    Actor actor = ActorCache.getActor(uuid);
    if (uuid == null || actor == null) {
      uuid = Guid.generate();
      System.out.println(uuid);
      ActorCache.addActor(uuid);
    }else {
      
    }
  }
  
  public static void Profile(RequestData rd) {
    System.out.println("Profile");
    System.out.println(rd);
  }

}
