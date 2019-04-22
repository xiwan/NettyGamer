package com.xiwan.NettyGamer.controller;

import com.xiwan.NettyGamer.App;
import com.xiwan.NettyGamer.Enum.ServerResult;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.entity.ResponseData;
import com.xiwan.NettyGamer.entity.ResponseData.ResponseDataBuilder;

import io.netty.channel.ChannelHandlerContext;

public abstract class BaseController {
  
  protected void SendData(ChannelHandlerContext ctx, ResponseData data) {
    App.gameServer.SendData(ctx, data);
  }
  
  protected ResponseData SuccessData(RequestData rd) {
    ResponseDataBuilder dataBuilder = ResponseData.builder().Type(ServerResult.Success.getCode())
        .Ticket(rd.getTicket());
    return dataBuilder.build();
  }

}
