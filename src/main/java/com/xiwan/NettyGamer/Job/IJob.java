package com.xiwan.NettyGamer.Job;

import java.util.ArrayList;
import java.util.List;

import com.xiwan.NettyGamer.utils.CustomJob;

public interface IJob {
  
  public static List<CustomJob> jobList = new ArrayList<>();
  
  public void init();

  public void job();
}
