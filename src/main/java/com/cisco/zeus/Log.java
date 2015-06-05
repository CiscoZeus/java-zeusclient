package com.cisco.zeus;

import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.IOException;


public class Log {
    JSONObject data = new JSONObject();
    //String logName ="";

    /*
    public Log(String name) {
        logName = name
    }
    
    public Log(double timestamp, String message) {
        data.put("timestamp",timestamp);
        data.put("message",message);
    }

    public Log setValue(String message){
        data.put("message",message);
        return this;
    }

     public Log setTimestamp(double timestamp){
        data.put("timestamp",timestamp);
        return this;
    }*/

    public Log setKeyValues(String key, String value){
        data.put(key,value);
        return this;
    }

    public Log setKeyValues(String key, double value){
        data.put(key,value);
        return this;
    }

     @Override
     public String toString() {
        return "["+data.toString()+"]";
    }

}



