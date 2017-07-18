package com.cisco.zeus;

import java.util.ArrayList;


public class  LogList {
    private ArrayList<Log> list;
    String logName;

    public LogList(String name) {
        list = new ArrayList<>();
        logName = name;
    }

    public LogList add(Log log) {
        list.add(log);
        return this;
    }

    public LogList build() {
        return this;
    }

    public void clear() {
        list = new ArrayList<>();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
