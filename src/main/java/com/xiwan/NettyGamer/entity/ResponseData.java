package com.xiwan.NettyGamer.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.protobuf.ByteString;
import com.xiwan.NettyGamer.proto.ServerResponse;

import lombok.Builder;
import lombok.Data;

@Builder
public class ResponseData {
  
  private int Type;
  private int Ticket;
  private List<Boolean> BoolList = new ArrayList<>();
  private List<Integer> IntList = new ArrayList<>();
  private List<Double> DoubleList = new ArrayList<>();
  private List<String> StringList = new ArrayList<>();
  private List<Long> DateTimeList = new ArrayList<>();
  private List<ByteString> BytesList = new ArrayList<>();
  private long ServerUnixTime;
  
  public void addString(String string) {
    if (StringList == null) {
      StringList = new ArrayList<>();
    }
    StringList.add(string);
  }
  
  public byte[] toByteArray(){
    ServerUnixTime = (new Date()).getTime();
    ServerResponse.payload.Builder builder = ServerResponse.payload.newBuilder();
    builder.setType(Type);
    builder.setTicket(Ticket);
    if (BoolList != null) builder.addAllBoolList(BoolList);
    if (IntList != null) builder.addAllIntList(IntList);
    if (DoubleList != null) builder.addAllDoubleList(DoubleList);
    if (StringList != null) builder.addAllStringList(StringList);
    if (DateTimeList != null) builder.addAllDateTimeList(DateTimeList);
    if (BytesList != null) builder.addAllByteList(BytesList);
    builder.addSeverUnixTime(ServerUnixTime);
    
    return builder.build().toByteArray();
  }
}
