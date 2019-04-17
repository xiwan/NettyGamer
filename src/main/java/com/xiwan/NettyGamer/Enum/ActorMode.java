package com.xiwan.NettyGamer.Enum;

public enum ActorMode {
  NONE("none"),
  PASSPORT("passport"),
  GAMER("gamer"),
  RAID("raid"),
  GM("gm"),
  SERVER("server");
  
  private String name;
  
  private ActorMode(String name) {
    this.name = name;
  }
}
