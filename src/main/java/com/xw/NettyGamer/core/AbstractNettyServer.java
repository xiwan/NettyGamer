package com.xw.NettyGamer.core;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.xw.NettyGamer.core.consumer.AbstractConsumer;
import com.xw.NettyGamer.core.consumer.IConsumer;
import com.xw.NettyGamer.core.factory.ReflectiveFactory;
import com.xw.NettyGamer.core.protocol.IProtocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;

public abstract class AbstractNettyServer<T extends ChannelHandler, E extends IProtocol> implements INettyServer<T, E> {

  private ChannelFuture f;
  private EventLoopGroup bossGroup;
  private EventLoopGroup workerGroup;
  
  private ReflectiveFactory<T> handler;
  @Getter
  private ReflectiveFactory<E> inProto;
  @Getter
  private ReflectiveFactory<E> outProto;
  
  protected int port;
  protected int maxPackageSize;
  protected int maxReaderIdleTime = 0;
  protected int maxWriterIdleTime = 0;
  protected int maxAllIdleTime = 0;
  protected List<IConsumer<?>> consumerList = new ArrayList<>();

  @Override
  public boolean Start() throws Exception {
    bossGroup = new NioEventLoopGroup(1);
    workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_LINGER, 5).childOption(ChannelOption.SO_KEEPALIVE, true)
          .childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              // TODO Auto-generated method stub
              ch.pipeline().addLast(new LengthFieldPrepender(4));
              ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(maxPackageSize, 0, 4, -4, 0));
              ch.pipeline().addLast(new IdleStateHandler(maxReaderIdleTime, maxWriterIdleTime, maxAllIdleTime));
              ch.pipeline().addLast(newHandler(consumerList));
            }

          });

      f = b.bind(port).sync();
      return f.isSuccess();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      throw e;
    }
  }

  @Override
  public boolean Stop() throws Exception {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public INettyServer<T, E> RegisterHandler(Class<T> klass) {
    
    this.handler = new ReflectiveFactory<T>(klass, List.class);
    return this;
  }
  
  @Override
  public INettyServer<T, E>  RegisterProtocol(Class<E> in, Class<E> out)
  {
    this.inProto = new ReflectiveFactory<E>(in);
    this.outProto = new ReflectiveFactory<E>(in);
    return this;
  }
  
  @Override
  public INettyServer<T, E> RegisterConsumer(AbstractConsumer<E> consumer) {
    if (this.inProto != null)
      consumer.in = this.inProto.newInstance();
    if (this.outProto != null)
      consumer.out = this.outProto.newInstance();
    consumerList.add(consumer);
    return this;
  }
  

  private ChannelHandler newHandler(List<?> consumerList){
    return this.handler.newInstance(consumerList);
  }

}
