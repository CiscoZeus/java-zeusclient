package com.cisco.zeus;

import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.ArrayList;
import java.io.IOException; 


public class MetricList {
    JSONObject data = new JSONObject();
    List<String> columnNames = new ArrayList<String>();
    List<List<Double>> columnValues = new ArrayList<List<Double>>();
    String metricName = "";
    JSONArray list = new JSONArray();

    public MetricList(String name) {
        metricName = name;
    }

    public MetricList setColumns(String... cols){
        for(String col : cols) {
            columnNames.add(col);
        }
        return this;
    }

    public MetricList setValues(double... values){
        List<Double> entry = new ArrayList<Double>();
 
        for(double value : values) {
            entry.add(value);
        }

        columnValues.add(entry);
        return this;
    }

    public MetricList build() throws IOException{
        boolean timePresent = false;
        double timestamp = 0; 
        for (int valueCount = 0; valueCount < columnValues.size(); valueCount++)
        {
            if(columnNames.size() !=  columnValues.get(valueCount).size())
                throw new IOException("Column name size and column value size do not match");
            for (int i = 0; i < columnNames.size(); i++) {
                if(columnNames.get(i).equals("timestamp")) {
                    timePresent = true;
                    timestamp = columnValues.get(valueCount).get(i);
                }
                else {
                data.put(columnNames.get(i),columnValues.get(valueCount).get(i));
                }
            }
            JSONObject datapoint = new JSONObject();
            datapoint.put("point",data);
            if(timePresent) {
                datapoint.put("timestamp",timestamp);
            }
            data = datapoint;
            list.add(data.clone());
            data.clear(); 

        }
        return this;
    }

    @Override
    public String toString() {
        return list.toString();
    }
}



