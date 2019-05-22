package com.xw.NettyGamer.core;

public enum EHandler {
  Unknown(0),
  Receive(1),
  Disconnect(2);
     
  private int code;
  
  private EHandler(int code){
    this.code = code;
  }
  
  public int getCode(){
    return this.code;
  }
  
}
