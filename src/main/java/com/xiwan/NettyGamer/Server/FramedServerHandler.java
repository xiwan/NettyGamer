package com.xiwan.NettyGamer.Server;

import java.util.function.Consumer;

import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.proto.ServerRequest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class FramedServerHandler extends ChannelInboundHandlerAdapter {

  private final int HEADER_LENGTH = 4;
  private final int TICKET_LENGTH = 4;
  private ByteBuf buf;
  private Consumer<RequestData> receiveAction;
  private Consumer<ChannelHandlerContext> disconnectAction;

  public FramedServerHandler(Consumer<RequestData> receiveAction, Consumer<ChannelHandlerContext> disconnectAction) {
    this.receiveAction = receiveAction;
    this.disconnectAction = disconnectAction;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    try {
      ByteBuf in = (ByteBuf) msg;
      buf = ctx.alloc().buffer(in.readableBytes());
      buf.writeBytes(in);
      if (buf.readableBytes() >= HEADER_LENGTH + TICKET_LENGTH) {
        //System.out.println(ByteBufUtil.hexDump(buf));
        int length = buf.readInt(); // HEADER_LENGTH
        int ticket = buf.readInt(); // TICKET_LENGTH
        byte[] b = new byte[buf.readableBytes()];
        buf.readBytes(b);
        ServerRequest.msgInfo rawData = ServerRequest.msgInfo.parseFrom(b);

        RequestData rd = new RequestData();
        rd.setTicket(ticket);
        rd.setActionType(rawData.getType());
        rd.setSocketContext(ctx);
        rd.setRawData(rawData);

        this.receiveAction.accept(rd);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      ReferenceCountUtil.release(msg);
      buf.release();
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    // TODO Auto-generated method stub
    ctx.flush();
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    // TODO Auto-generated method stub
    if (evt instanceof IdleStateEvent) {
      IdleState state = ((IdleStateEvent) evt).state();
      switch (state)
        {
        case ALL_IDLE:
          break;
        case WRITER_IDLE:
          break;
        case READER_IDLE:
          if (this.disconnectAction != null)
            this.disconnectAction.accept(ctx);
          break;
        }

    }
    super.userEventTriggered(ctx, evt);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    // TODO Auto-generated method stub
    super.exceptionCaught(ctx, cause);
    ctx.close();
  }

}
