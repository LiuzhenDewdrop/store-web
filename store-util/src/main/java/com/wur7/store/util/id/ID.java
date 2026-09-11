package com.wur7.store.util.id;

public interface ID<T> {
    /**
     * 生成新的ID
     * @return
     */
    T nextId();
}
