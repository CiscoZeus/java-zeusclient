package com.cisco.zeus;

import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Metric {
    JSONObject data = new JSONObject();

    public Metric(double value) {
        data.put("value",value);
    }

    public Metric(double timestamp, double value) {
        data.put("timestamp",timestamp);
        data.put("value",value);
    }

    public Metric setValue(double value){
        data.put("value",value);
        return this;
    }

     public Metric setTimestamp(double timestamp){
        data.put("timestamp",timestamp);
        return this;
    }

     @Override
     public String toString() {
        return "["+data.toString()+"]";
    }

}



