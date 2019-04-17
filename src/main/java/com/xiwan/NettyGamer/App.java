package com.xiwan.NettyGamer;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xiwan.NettyGamer.Enum.ServerCmd;
import com.xiwan.NettyGamer.base.GameServer;

/**
 * Hello world!
 *
 */
public class App {

  public static void main(String[] args) {
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
  }

  private static void ShutdownServer() {
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    singleThreadExecutor.execute(new Runnable() {
      @Override
      public void run() {
        GameServer.Instance().ShutdownServer();
        System.exit(0);
      }
    });
  }

}
