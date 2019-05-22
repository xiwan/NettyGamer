package com.xw.NettyGamer.core;
import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.proto.ServerRequest.msgInfo;
import com.xw.NettyGamer.core.protocol.IProtocol;
import com.xw.NettyGamer.core.protocol.RequestProtocol;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

public class NettyServer<T extends ChannelHandler, E extends IProtocol> extends AbstractNettyServer<T, E> {
  
  public NettyServer(int port, int maxPackageSize, int maxReaderIdleTime, int maxWriterIdleTime, int maxAllIdleTime) {
    this.port = port;
    this.maxPackageSize = maxPackageSize;
    this.maxReaderIdleTime = maxReaderIdleTime;
    this.maxWriterIdleTime = maxWriterIdleTime;
    this.maxAllIdleTime = maxAllIdleTime;
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    try {
      this.Start();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void ReceiveAction(RequestProtocol<msgInfo> rd){
    System.out.println(rd);
  }
  
  public void DisconnectAction(ChannelHandlerContext ctx) {
    System.out.println(ctx);
  }

}
