package com.xiwan.NettyGamer;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Callable;
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

  public static void main(String[] args) {
    logger.info("xxxxx");
    System.out.println("Please type command:");
    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      String cmd = scanner.nextLine();
      App.runCommand(cmd.trim());
    }
    scanner.close();
  }

  public static void runCommand(String cmd) {
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
    
    System.out.println("done");
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
      System.out.println(String.format("exit: %s", result));
    } catch (InterruptedException | ExecutionException e) {
      task.cancel(true);
    } finally {
      singleThreadExecutor.shutdown();
      System.exit(0);
    }
  }

}
