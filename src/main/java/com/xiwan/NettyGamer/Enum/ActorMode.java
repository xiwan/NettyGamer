package com.xiwan.NettyGamer.Enum;

import lombok.Getter;

public enum ActorMode {
  NONE("none"),
  PASSPORT("passport"),
  GAMER("gamer"),
  RAID("raid"),
  GM("gm"),
  SERVER("server");
  
  @Getter
  private String name;
  
  private ActorMode(String name) {
    this.name = name;
  }
}
