package com.xw.NettyGamer.core.protocol;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestProtocol<T> implements IProtocol {

  private int Ticket;
  private String ServerID;
  private int ActionType;
  private ChannelHandlerContext socketContext;
  private T rawData;

}
