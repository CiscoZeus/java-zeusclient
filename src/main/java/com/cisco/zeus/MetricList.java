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


    public void add(double timestamp, double value) {
        Metric metric = new Metric(timestamp,value);
        list.add(metric.data);
    }
    
    public void add(double value) {
        Metric metric = new Metric(value);
        list.add(metric.data);
    }    

    public void clear(){
        list = new JSONArray();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}



