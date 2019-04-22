package com.xiwan.NettyGamer.entity;

import java.util.Date;

import lombok.Data;

@Data
public class LogData {
  
  protected String DateTime;

  protected String Content;
  
  public LogData() {
    java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    this.DateTime = format.format(new Date());
  }
  
}
