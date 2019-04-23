package com.xiwan.NettyGamer.utils;

import java.text.DateFormat;
import java.util.Date;

public class DateTimeHelper {
  
  private static DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  
  public static String convertDateTime(Date date) {
    return format1.format(new Date());
  }

}
