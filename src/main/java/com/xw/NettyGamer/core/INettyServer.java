package com.xw.NettyGamer.core;

import com.xw.NettyGamer.core.consumer.AbstractConsumer;
import com.xw.NettyGamer.core.consumer.IConsumer;

public interface INettyServer<T, E> extends IServer, Runnable {
  
  INettyServer<T, E>  RegisterProtocol(Class<E> in, Class<E> out);
  
  INettyServer<T, E> RegisterHandler(Class<T> klass);

  INettyServer<T, E> RegisterConsumer(AbstractConsumer<E> consumer);

}
