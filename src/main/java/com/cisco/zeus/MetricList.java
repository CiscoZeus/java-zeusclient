package com.cisco.zeus;

import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class MetricList {
    
    JSONArray list = new JSONArray();

    public MetricList(){
    }

    public MetricList(Metric metric){
        list.add(metric.data);
    }

    public void add(Metric metric){
        list.add(metric.data.clone());
    }

    @Override
    public String toString() {
        return list.toString();
    }
}



