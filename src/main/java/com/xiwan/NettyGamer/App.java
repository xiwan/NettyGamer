package com.xiwan.NettyGamer;

import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import com.xiwan.NettyGamer.Enum.ServerCmd;
import com.xiwan.NettyGamer.base.GameServer;
import com.xiwan.NettyGamer.utils.LogHelper;

/**
 * Hello world!
 *
 */
public class App {
  
  static CountDownLatch latch = new CountDownLatch(5);
  static ExecutorService ex = Executors.newCachedThreadPool(new CustomThreadFactory(App.class.getSimpleName()));
  // ExecutorCompletionService cs = new ExecutorCompletionService(ex);

  public static void main(String[] args) {
    StartCountdown();
    DefaultCommand();
    ParseCommand();
  }

  private static void ParseCommand() {
    LogHelper.WriteInfoLog("Please type command [auto-start in 5 secs]:");

    Runnable task = new Runnable() {
      @Override
      public void run() {
        // TODO Auto-generated method stub
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
          String cmd = scanner.nextLine();
          App.runCommand(cmd.trim());
        }
        scanner.close();
      }
    };
    ex.execute(task);
  }

  private static void DefaultCommand() {
    Runnable task = new Runnable() {
      @Override
      public void run() {
        // TODO Auto-generated method stub
        if (latch.getCount() == 0)
          return;
        try {
          latch.await();
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        String cmd = "start";
        App.runCommand(cmd);
      }
    };
    ex.execute(task);
  }

  private static void StartCountdown() {
    Runnable task = new Runnable() {
      @Override
      public void run() {
        // TODO Auto-generated method stub
        while (latch.getCount() > 0) {
          latch.countDown();
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    };
    ex.execute(task);
  }
  

  private static void runCommand(String cmd) {
    LogHelper.WriteDebugLog("exec: " + cmd);
    switch (ServerCmd.getCmd(cmd))
      {
      case START:
        StartServer();
        break;
      case STOP:
        ShutdownServer();
        break;
      case SHUTDOWN:
        ShutdownServer();
        break;
      default:
        break;
      }
  }

  private static void StartServer() {
    if (GameServer.Instance().isRunning) {
      LogHelper.WriteDebugLog("server is running...");
      return;
    }

    GameServer.Instance().Initialize("");
    GameServer.Instance().StartServer();
    GameServer.Instance().StartTimer();
    GameServer.Instance().isRunning = true;
    
    LogHelper.WriteInfoLog(String.format("start [%s]", GameServer.Instance().isRunning));
  }

  private static void ShutdownServer() {
    try {
      GameServer.Instance().ShutdownServer();
      GameServer.Instance().isRunning = false;
      LogHelper.WriteInfoLog(String.format("exit [%s]", !GameServer.Instance().isRunning));
      
    } finally {
      ex.shutdown();
      try {
        LogHelper.FlushLog();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      System.exit(0);
    }
  }

}
