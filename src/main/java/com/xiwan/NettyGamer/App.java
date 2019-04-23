package com.xiwan.NettyGamer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xiwan.NettyGamer.Enum.ServerCmd;
import com.xiwan.NettyGamer.Enum.ServerEnv;
import com.xiwan.NettyGamer.base.GameServer;
import com.xiwan.NettyGamer.utils.CustomThreadFactory;
import com.xiwan.NettyGamer.utils.LogHelper;

public class App {
  static ExecutorService ex;
  static String env;
  static String cmd;
  public static GameServer gameServer;

  static {
    ex = Executors.newCachedThreadPool(new CustomThreadFactory(App.class.getSimpleName()));
  }

  public static void main(String[] args) throws ParseException, FileNotFoundException {
    ParseCommandLine(args);
    InitLogConfig();
    InitGamerServer();
  }

  private static void ParseCommandLine(String[] args) throws ParseException {
    Options options = new Options();
    options.addOption("h", "help", false, "Print this usage information");
    options.addOption("v", "verbose", false, "Print out VERBOSE information");
    // options.addOption("f", "file", true, "File to save program output to");
    @SuppressWarnings("static-access")
    Option property = OptionBuilder.withArgName("property=value").hasArgs(2).withValueSeparator()
        .withDescription("use value for given property(property=value)").create("D");
    property.setRequired(false);
    options.addOption(property);

    // print usage
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("AntOptsCommonsCLI", options);

    CommandLineParser parser = new PosixParser();
    CommandLine cmdLine = parser.parse(options, args);

    String env = cmdLine.getOptionProperties("D").getProperty("nettygamer.env");
    String cmd = cmdLine.getOptionProperties("D").getProperty("nettygamer.cmd");

    App.env = ServerEnv.getEnv(env).name().toLowerCase();
    App.cmd = ServerCmd.getCmd(cmd).name().toLowerCase();

  }

  private static void InitLogConfig() throws FileNotFoundException {
    String separator = System.getProperty("file.separator");
    String logConfigRelative = String.format("src%smain%sresources", separator, separator);
    if (!App.env.equalsIgnoreCase(ServerEnv.DEV.name())) {
      logConfigRelative = "config";
    }
    String logConfigFileName = String.format("log4j2.%s.xml", App.env);
    String logConfigAbusolutePath = System.getProperty("user.dir") + separator + logConfigRelative + separator + logConfigFileName;
    System.out.println(logConfigAbusolutePath);
    File log4jFile = new File(logConfigAbusolutePath);
    if (log4jFile.exists()) {
      ConfigurationSource source = new ConfigurationSource(new FileInputStream(log4jFile), log4jFile);
      Configurator.initialize(null, source);
    }
  }

  private static void InitGamerServer() {
    String contextFileName = String.format("app-context.%s.xml", App.env);
    @SuppressWarnings("resource")
    AbstractApplicationContext context = new ClassPathXmlApplicationContext(contextFileName);
    gameServer = context.getBean("GameServer", GameServer.class);

    CountDownLatch latch = new CountDownLatch(5);
    StartCountdown(latch);
    RunDefaultCommand(latch);
    ParseScannerIn();
  }

  private static void ParseScannerIn() {
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

  private static void RunDefaultCommand(CountDownLatch latch) {
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
        App.runCommand(App.cmd);
      }
    };
    ex.execute(task);
  }

  private static void StartCountdown(CountDownLatch latch) {
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
        StopServer();
        break;
      case SHUTDOWN:
        ShutdownServer();
        break;
      default:
        LogHelper.WriteErrorLog("bad cmd");
        break;
      }
  }

  private static void StartServer() {
    if (gameServer.isRunning) {
      LogHelper.WriteDebugLog("server is running");
      return;
    }

    gameServer.Initialize("");
    gameServer.StartServer();
    gameServer.StartTimer();
    gameServer.isRunning = true;

    LogHelper.WriteInfoLog(String.format("Command Line env=[%s] cmd=[%s]", App.env, App.cmd));
    LogHelper.WriteInfoLog(String.format("start [%s]", gameServer.isRunning));
  }
  
  private static void StopServer() {
    if (!gameServer.isRunning) {
      LogHelper.WriteDebugLog("server has stopped");
      return;
    }
    gameServer.ShutdownServer();
    gameServer.isRunning = false;
    LogHelper.WriteInfoLog(String.format("exit [%s]", !gameServer.isRunning));
    try {
      LogHelper.FlushLog();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void ShutdownServer() {
    StopServer();
    ex.shutdown();
    System.exit(0);
  }

}
