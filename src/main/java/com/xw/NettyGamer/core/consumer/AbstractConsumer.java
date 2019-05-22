package com.xw.NettyGamer.core.consumer;

import java.util.function.Consumer;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

public abstract class AbstractConsumer<T> implements IConsumer<T> {
  
  protected Consumer<T> consumer;
  
  public T rd;
  public T in;
  public T out;
  
  public AbstractConsumer(Consumer<T> consumer) {
    this.consumer = consumer;
  }
  
  @Override
  public void Consume(T t) {
    // TODO Auto-generated method stub
    consumer.accept(t);
  }
  
  abstract void Execute(ChannelHandlerContext ctx, Object obj) throws Exception;

}
