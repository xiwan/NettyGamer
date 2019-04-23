package com.xiwan.NettyGamer.entity;

import java.util.Date;

import com.xiwan.NettyGamer.Enum.LogType;
import com.xiwan.NettyGamer.utils.DateTimeHelper;

import lombok.Data;

@Data
public class LogData {
  
  protected int Type;
  
  protected String DateTime;

  protected String Content;
  
  public LogData() {
    this.Type = LogType.Unknown.getCode();
    this.DateTime = DateTimeHelper.convertDateTime(new Date());
  }
  
}
