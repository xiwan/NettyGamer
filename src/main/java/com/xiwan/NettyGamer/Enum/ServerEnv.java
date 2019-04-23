package com.xiwan.NettyGamer.Enum;

public enum ServerEnv {
  DEV("dev"), TEST("test"), STAGING("staging"), PRODUCTION("production"), DEFAULT("dev");
  
  private String name;

  private ServerEnv(String name) {
    this.name = name;
  }

  public static ServerEnv getEnv(String name) {
    for (ServerEnv env : ServerEnv.values()) {
      if (env.name.equalsIgnoreCase(name)) {
        return env;
      }
    }
    return DEFAULT;
  }
}
