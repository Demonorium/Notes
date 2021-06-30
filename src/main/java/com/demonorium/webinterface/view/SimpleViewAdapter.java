package com.demonorium.webinterface.view;


public class SimpleViewAdapter<T> {
    T object;

    public SimpleViewAdapter() {
    }

    public SimpleViewAdapter(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
