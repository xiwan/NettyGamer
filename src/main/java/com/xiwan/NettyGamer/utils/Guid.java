package com.xiwan.NettyGamer.utils;

import java.util.UUID;

public class Guid {

  public static String generate() {
    UUID uuid = UUID.randomUUID();
    String randomUUIDString = uuid.toString();
    return randomUUIDString.replace("-", "");
  }
}
