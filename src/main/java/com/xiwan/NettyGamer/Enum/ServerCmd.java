package com.xiwan.NettyGamer.Enum;

public enum ServerCmd {

  START("start"), STOP("stop"), SHUTDOWN("shutdown"), NULL("null");

  private String cmd;

  private ServerCmd(String cmd) {
    this.cmd = cmd;
  }

  public static ServerCmd getCmd(String cmd) {
    for (ServerCmd scmd : ServerCmd.values()) {
      if (scmd.cmd.equalsIgnoreCase(cmd)) {
        return scmd;
      }
    }
    return NULL;
  }

}
