package com.cisco.zeus;

import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Parameters {

    public HashMap<String,Object> data = new HashMap<>();
    
    public Parameters add(String key, Object value) {
        data.put(key,value);
        return this;
    }

    public void clear() {
        data.clear();
    }

}
