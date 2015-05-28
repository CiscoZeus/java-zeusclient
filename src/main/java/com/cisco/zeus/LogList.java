package com.cisco.zeus;

import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class LogList {
    
    JSONArray list = new JSONArray();

    public LogList(){
    }

    public LogList(Log log){
        list.add(log.data);
    }


    public void add(double timestamp, String message) {
        Log log = new Log(timestamp,message);
        list.add(log.data);
    }
    
    public void add(String message) {
        Log log = new Log(message);
        list.add(log.data);
    }    

    public void clear(){
        list = new JSONArray();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}



