package com.xw.NettyGamer.core.consumer;

import java.util.function.Consumer;

import com.xiwan.NettyGamer.entity.RequestData;
import com.xiwan.NettyGamer.proto.ServerRequest;
import com.xiwan.NettyGamer.proto.ServerRequest.msgInfo;
import com.xw.NettyGamer.core.factory.ReflectiveFactory;
import com.xw.NettyGamer.core.protocol.RequestProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class RequestConsumer extends AbstractConsumer<RequestProtocol> {

  private final int HEADER_LENGTH = 4;
  private final int TICKET_LENGTH = 4;

  public RequestConsumer(Consumer<RequestProtocol> action) {
    super(action);
  }

  @Override
  public void Execute(ChannelHandlerContext ctx, Object msg) throws Exception{
    try {
      ByteBuf inBuf = (ByteBuf) msg;
      ByteBuf buf = ctx.alloc().buffer(inBuf.readableBytes());
      buf.writeBytes(inBuf);
      if (buf.readableBytes() >= HEADER_LENGTH + TICKET_LENGTH) {
        // System.out.println(ByteBufUtil.hexDump(buf));
        int length = buf.readInt(); // HEADER_LENGTH
        int ticket = buf.readInt(); // TICKET_LENGTH
        byte[] b = new byte[buf.readableBytes()];
        buf.readBytes(b);

        ServerRequest.msgInfo rawData = ServerRequest.msgInfo.parseFrom(b);
        in.setTicket(ticket);
        in.setActionType(rawData.getType());
        in.setSocketContext(ctx);
        in.setRawData(rawData);
        this.Consume(in);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }
}
