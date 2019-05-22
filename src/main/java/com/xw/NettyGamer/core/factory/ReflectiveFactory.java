package com.xw.NettyGamer.core.factory;

import java.lang.reflect.Constructor;

import io.netty.util.internal.StringUtil;

public class ReflectiveFactory<T> implements IReflectiveFactory<T> {
  
  private Constructor<T> constructor;
  
  public ReflectiveFactory(Class<T> klazz, Class<?> ... paramKlazz) {
    if (klazz == null) {
      throw new NullPointerException("empty klazz");
    }
    try {
      this.constructor = klazz.getConstructor(paramKlazz);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new IllegalArgumentException("Class " + StringUtil.simpleClassName(klazz) +
          " does not have a public non-arg constructor", e);
    }
  }

  public ReflectiveFactory(Class<T> klazz) {
    this(klazz, null);
  }

  @Override
  public T newInstance(Object... parameters) {
    try {
      return this.constructor.newInstance(parameters);
    } catch (Throwable t) {
      throw new RuntimeException("Unable to create Channel from class " + constructor.getDeclaringClass(), t);
    }
  }

}
