package com.prathamubs.meridukan.db;

import java.util.List;

public interface DataSource<T> {
    void insert(T item);
    List<T> getAll();
}