package com.xiwan.NettyGamer.Client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.xiwan.NettyGamer.entity.RequestData;

import lombok.Getter;

public class SystemCache {

  private final static int REQUEST_QUEUE_LENGTH = 1024;
  @Getter
  private final static BlockingQueue<RequestData> requestQueue = new ArrayBlockingQueue<RequestData>(
      REQUEST_QUEUE_LENGTH);
  
  
  public static boolean PushRequest(RequestData rd) {
    if (requestQueue.remainingCapacity() < Math.round(REQUEST_QUEUE_LENGTH * 0.2)) {
      return false;
    }
    return requestQueue.offer(rd);
  }
  
  
}
