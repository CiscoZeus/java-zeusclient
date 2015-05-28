package com.cisco.zeus;

import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Log {
    JSONObject data = new JSONObject();

    public Log(String message) {
        data.put("message",message);
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
    }

     @Override
     public String toString() {
        return "["+data.toString()+"]";
    }

}



