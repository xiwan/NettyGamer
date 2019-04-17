package com.xiwan.NettyGamer.entity;

import com.xiwan.NettyGamer.proto.ServerRequest;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class RequestData {
  
  private int Ticket;
  private String ServerID;
  private int ActionType;
  private ChannelHandlerContext socketContext;
  private ServerRequest.msgInfo rawData;
}
