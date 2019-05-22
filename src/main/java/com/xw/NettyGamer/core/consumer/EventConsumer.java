package com.xw.NettyGamer.core.consumer;

import java.util.function.Consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class EventConsumer extends AbstractConsumer<ChannelHandlerContext> {

  public EventConsumer(Consumer<ChannelHandlerContext> consumer) {
    super(consumer);
  }

  @Override
  void Execute(ChannelHandlerContext ctx, Object evt) throws Exception {
    // TODO Auto-generated method stub
    if (evt instanceof IdleStateEvent) {
      this.rd = ctx;
      IdleState state = ((IdleStateEvent) evt).state();
      switch (state)
        {
        case ALL_IDLE:
          break;
        case WRITER_IDLE:
          break;
        case READER_IDLE:
          this.Consume(this.rd);
          break;
        }

    }
  }

}
