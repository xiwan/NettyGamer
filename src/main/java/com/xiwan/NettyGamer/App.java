package com.xiwan.NettyGamer;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
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
  static CountDownLatch latch = new CountDownLatch(3);

  public static void main(String[] args) { 
    System.out.println("Please type command [auto-start in 3 secs]:");
    StartCountdown();
    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      String cmd = scanner.nextLine();
      App.runCommand(cmd.trim());
    }
    scanner.close();
  }

  public static void runCommand(String cmd) {
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
    GameServer.Instance().Initialize("");
    GameServer.Instance().StartServer();
    GameServer.Instance().StartTimer();
    logger.info("done");
  }

  private static void ShutdownServer() {
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    Future<Boolean> task = singleThreadExecutor.submit(new Callable<Boolean>() {
      @Override
      public Boolean call() throws Exception {
        GameServer.Instance().ShutdownServer();
        return true;
      }
    });
    try {
      boolean result = task.get() || task.isDone();
      logger.info(String.format("exit: %s", result));
    } catch (InterruptedException | ExecutionException e) {
      task.cancel(true);
    } finally {
      singleThreadExecutor.shutdown();
      System.exit(0);
    }
  }

  private static void StartCountdown() {
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    singleThreadExecutor.execute(new Runnable() {
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
    });
    
    if (latch.getCount() > 0) {
      try {
        latch.await();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      String cmd = "start";
      App.runCommand(cmd);
    }
  }

}
