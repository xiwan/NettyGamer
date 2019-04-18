package com.xiwan.NettyGamer.entity;

import java.util.function.Consumer;

import com.xiwan.NettyGamer.Enum.ActorMode;

import lombok.Data;

@Data
public class RequestRoute {
  
  private int priority;
  private Consumer<RequestData> action;
  private ActorMode actorMode;
}
