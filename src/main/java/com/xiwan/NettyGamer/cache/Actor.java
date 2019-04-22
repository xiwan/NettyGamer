package com.xiwan.NettyGamer.cache;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

import com.xiwan.NettyGamer.entity.RequestData;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

public class Actor {

  @Setter
  @Getter
  private String uniqueID;
  
  @Setter
  @Getter
  private Future<Boolean> currentTask;

  private final int REQUEST_QUEUE_LENGTH = 2048;
  @Getter
  private BlockingQueue<RequestData> requestQueue = new ArrayBlockingQueue<RequestData>(REQUEST_QUEUE_LENGTH);
  
  public void PushRequest(int ticket, int actionType, Consumer<RequestData> action, ChannelHandlerContext socketContext) {
    RequestData rd = new RequestData();
    rd.setTicket(ticket);
    rd.setActionType(actionType);
    rd.setSocketContext(socketContext);
    PushRequest(rd);
  }
  
  public boolean PushRequest(RequestData rd) {
    if (requestQueue.remainingCapacity() < Math.round(REQUEST_QUEUE_LENGTH * 0.2)) {
      return false;
    }
    return requestQueue.offer(rd);
  }

}
