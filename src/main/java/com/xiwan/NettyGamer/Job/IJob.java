package com.xiwan.NettyGamer.Job;

import java.util.ArrayList;
import java.util.List;

public interface IJob {
  
  public void init();

  public void job();
  
  public void shutdown();
}
