package com.mileworks.gen.common.function;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
