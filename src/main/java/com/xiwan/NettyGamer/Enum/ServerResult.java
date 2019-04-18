package com.xiwan.NettyGamer.Enum;

import lombok.Getter;

public enum ServerResult {
  Unknown(0xFF0000),
  Success(0xFF0001),
  Fail(0xFF0002),
  InvalidAuthority(0xFF0003),
  MessageStart(0xFF0004),
  MessageEnd(0xFF0005),
  InvalidActor(0xFF0006),
  ServerError(0xFF0007),
  Debug(0xFF0009),
  Passport_InvalidAuth(0xFF0010),
  Passport_InvalidToken(0xFF0011),
  Passport_InvalidUser(0xFF0012);
  
  @Getter
  private int code;
  
  private ServerResult(int code) {
    this.code = code;
  }
  
  
}
