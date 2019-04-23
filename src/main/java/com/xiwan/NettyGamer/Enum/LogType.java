package com.xiwan.NettyGamer.Enum;

import lombok.Getter;

public enum LogType {
  Unknown(0, "Unknown"),
  Create(1, "Create"),
  Login(2, "Login"),
  Logout(3, "Logout"),
  Charge(4, "Charge"),
  ;
  @Getter
  private int code;
  private String name;
  
  private LogType(int code, String name) {
    this.code = code;
    this.name= name;
  }
}
