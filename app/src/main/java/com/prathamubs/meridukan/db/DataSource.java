package com.prathamubs.meridukan.db;

import java.util.List;

public interface DataSource<T> {
    void insert(T item);
    void update(T item);
    void delete(T...items);
    List<T> getAll();
}