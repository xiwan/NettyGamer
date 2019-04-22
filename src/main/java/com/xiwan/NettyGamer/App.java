package com.xiwan.NettyGamer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.xiwan.NettyGamer.Enum.ServerCmd;
import com.xiwan.NettyGamer.base.GameServer;

/**
 * Hello world!
 *
 */
public class App {
  static final Logger logger = LogManager.getLogger(App.class.getName());
  static CountDownLatch latch = new CountDownLatch(5);
  static ExecutorService ex = Executors.newCachedThreadPool(new CustomThreadFactory(App.class.getSimpleName()));
  // ExecutorCompletionService cs = new ExecutorCompletionService(ex);

  public static void main(String[] args) {
    StartCountdown();
    DefaultCommand();
    ParseCommand();
  }

  private static void ParseCommand() {
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
    logger.info("Please type command [auto-start in 5 secs]:");
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
    logger.info("exec: " + cmd);
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
      logger.info("server is running...");
      return;
    }

    GameServer.Instance().Initialize("");
    GameServer.Instance().StartServer();
    GameServer.Instance().StartTimer();
    GameServer.Instance().isRunning = true;
    logger.info("done");
  }

  private static void ShutdownServer() {
    Future<Boolean> task = ex.submit(new Callable<Boolean>() {
      @Override
      public Boolean call() throws Exception {
        GameServer.Instance().ShutdownServer();
        GameServer.Instance().isRunning = false;
        return true;
      }
    });
    try {
      boolean result = task.get() || task.isDone();
      logger.info(String.format("running [%s] exit [%s]", GameServer.Instance().isRunning, result));
    } catch (InterruptedException | ExecutionException e) {
      task.cancel(true);
    } finally {
      ex.shutdown();
      System.exit(0);
    }
  }

}
