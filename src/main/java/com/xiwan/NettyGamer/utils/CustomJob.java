package com.xiwan.NettyGamer.utils;

import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.Data;

@Data
public class CustomJob implements Runnable {
  private String name;
  private int timeout;
  private int delay;
  private Consumer consumerTask;
  private Function functionTask;
  private Supplier supplierTask;
  private Predicate predicateTask;
  private Future currentTask;

  @Override
  public void run() {
    // TODO Auto-generated method stub
    if (consumerTask != null)
      consumerTask.accept(null);
    if (functionTask != null)
      functionTask.apply(null);
    if (supplierTask != null)
      supplierTask.get();
    if (predicateTask != null)
      predicateTask.test(null);
  }

}