package com.xiwan.NettyGamer.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.xiwan.NettyGamer.App;
import com.xiwan.NettyGamer.Enum.ServerResult;
import com.xiwan.NettyGamer.base.GameServer;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.ResponseData;
import com.xiwan.NettyGamer.entity.ResponseData.ResponseDataBuilder;

import io.netty.channel.ChannelHandlerContext;

public class BaseController {
  
  protected static final Logger logger = LogManager.getLogger(BaseController.class);
  
  protected void SendData(ChannelHandlerContext ctx, ResponseData data) {
    GameServer.Instance().SendData(ctx, data);
  }
  
  protected ResponseData SuccessData(RequestData rd) {
    ResponseDataBuilder dataBuilder = ResponseData.builder().Type(ServerResult.Success.getCode())
        .Ticket(rd.getTicket());
    return dataBuilder.build();
  }

}
