package com.erc.log.http;

import java.util.ArrayList;

public interface CrudOperation<T> {
    ArrayList<T> getAll();

    T post(T item);
}
