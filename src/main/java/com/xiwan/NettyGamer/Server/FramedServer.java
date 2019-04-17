package com.xiwan.NettyGamer.Server;

import java.util.function.Consumer;

import com.xiwan.NettyGamer.entity.RequestData;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class FramedServer {

  private ChannelFuture f;
  private EventLoopGroup bossGroup;
  private EventLoopGroup workerGroup;
  private int port;
  private int maxPackageSize;
  private Consumer<RequestData> receiveAction;
  private Consumer<ChannelHandlerContext> disconnectAction;

  public FramedServer(int port, int maxPackageSize, Consumer<RequestData> receiveAction,
      Consumer<ChannelHandlerContext> disconnectAction) {
    this.port = port;
    this.maxPackageSize = maxPackageSize;
    this.receiveAction = receiveAction;
    this.disconnectAction = disconnectAction;
  }

  public void run() throws Exception {
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
              ch.pipeline().addLast(new FramedServerHandler(receiveAction, disconnectAction));
            }

          });

      f = b.bind(port).sync();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      throw e;
    }

  }

  public void stop() throws Exception {
    // Wait until the server socket is closed.
    try {
      workerGroup.shutdownGracefully().sync();
      bossGroup.shutdownGracefully().sync();
      f.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      throw e;
    }

  }

}
