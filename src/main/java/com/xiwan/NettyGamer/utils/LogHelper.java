package com.xiwan.NettyGamer.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.xiwan.NettyGamer.CustomThreadFactory;
import com.xiwan.NettyGamer.entity.LogData;


public class LogHelper {
  private static final Logger logger = LogManager.getLogger();

  static ExecutorService ex = Executors.newCachedThreadPool(new CustomThreadFactory(LogHelper.class.getSimpleName()));
  private final static int QUEUE_LENGTH = 100000;

  private final static BlockingQueue<String> infoQueue;
  private final static BlockingQueue<String> debugQueue;
  private final static BlockingQueue<String> errorQueue;

  static {
    infoQueue = new ArrayBlockingQueue<String>(QUEUE_LENGTH);
    debugQueue = new ArrayBlockingQueue<String>(QUEUE_LENGTH);
    errorQueue = new ArrayBlockingQueue<String>(QUEUE_LENGTH);
  }

  private static void CommonLogEnqueue(BlockingQueue<String> logQueue, String content) {
    if (content == null || content.length() == 0) {
      return;
    }
    logQueue.offer(content);
  }

  public static void WriteInfoLog(String log) {
    LogData logData = new LogData();
    logData.setContent(log);
    CommonLogEnqueue(infoQueue, log);
  }
  
  public static void WriteInfoLog(LogData log) {
    CommonLogEnqueue(infoQueue, JSON.toJSONString(log));
  }

  public static void WriteDebugLog(String log) {
    CommonLogEnqueue(debugQueue, log);
  }
  
  public static void WriteErrorLog(String log) {
    CommonLogEnqueue(errorQueue, log);
  }
  
  public static void FlushLog() throws InterruptedException {
    while(infoQueue.size() > 0 || debugQueue.size() > 0 || errorQueue.size() > 0)
      Thread.sleep(1000);
    ex.shutdown();
  }

  public static void SaveLog() {

    Runnable infoTask = new Runnable() {

      @Override
      public void run() {
        // TODO Auto-generated method stub
        String content = infoQueue.poll();
        while (content != null) {
          logger.info(content);
          content = infoQueue.poll();
        }
      }
    };
    Runnable errorTask = new Runnable() {

      @Override
      public void run() {
        // TODO Auto-generated method stub
        String content = errorQueue.poll();
        while (content != null) {
          logger.error(content);
          content = errorQueue.poll();
        }
      }
    };
    Runnable debugTask = new Runnable() {

      @Override
      public void run() {
        // TODO Auto-generated method stub
        String content = debugQueue.poll();
        while (content != null) {
          logger.debug(content);
          content = debugQueue.poll();
        }
      }
    };

    List<Runnable> taskList = new ArrayList<Runnable>();
    taskList.add(infoTask);
    taskList.add(debugTask);
    taskList.add(errorTask);

    for (Runnable task : taskList) {
      ex.execute(task);
    }
  }

}
