package com.xw.NettyGamer.core;

import java.util.List;

import com.xw.NettyGamer.core.consumer.AbstractConsumer;
import com.xw.NettyGamer.core.consumer.EventConsumer;
import com.xw.NettyGamer.core.consumer.RequestConsumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
  
  private List<AbstractConsumer<?>> ConsumerList;

  public NettyServerHandler(List<AbstractConsumer<?>> ConsumerList) {
    this.ConsumerList = ConsumerList;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    for (AbstractConsumer<?> consumer : ConsumerList) {
      if (consumer instanceof RequestConsumer) {
        ((RequestConsumer)consumer).Execute(ctx, msg);
      }
    }
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    // TODO Auto-generated method stub
    for (AbstractConsumer<?> consumer : ConsumerList) {
      if (consumer instanceof EventConsumer) {
        ((RequestConsumer)consumer).Execute(ctx, evt);
      }
    }
    super.userEventTriggered(ctx, evt);
  }
  

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    // TODO Auto-generated method stub
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    // TODO Auto-generated method stub
    super.exceptionCaught(ctx, cause);
    ctx.close();
  }

}
