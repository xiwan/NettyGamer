package com.xiwan.NettyGamer.controller.none;

import com.xiwan.NettyGamer.cache.Actor;
import com.xiwan.NettyGamer.cache.ActorCache;
import com.xiwan.NettyGamer.controller.BaseController;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.ResponseData;
import com.xiwan.NettyGamer.utils.Guid;
import com.xiwan.NettyGamer.utils.LogHelper;

public class TestController extends BaseController {
  
  public void Login(RequestData rd) {
    String uuid = rd.getRawData().getVarTable(0);
    Actor actor = ActorCache.getActor(uuid);
    if (uuid == null || actor == null) {
      uuid = Guid.generate();
      ActorCache.addActor(uuid);
    }else {
      
    }
    
    LogHelper.WriteInfoLog(uuid);
    ResponseData data = SuccessData(rd);
    data.addString(uuid);
    SendData(rd.getSocketContext(), data);
  }
  
  public void Profile(RequestData rd) {

  }

}
