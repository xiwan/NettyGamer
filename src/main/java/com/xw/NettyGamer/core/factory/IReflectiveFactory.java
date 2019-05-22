package com.xw.NettyGamer.core.factory;

public interface IReflectiveFactory<T> {

  T newInstance(Object[] parameters);
}
