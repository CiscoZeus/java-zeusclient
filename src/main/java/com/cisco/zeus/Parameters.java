package com.cisco.zeus;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.util.*;


public class Parameters {

    public HashMap<String, Object> data = new HashMap<>();

    public void add(String key, Object value) {
        data.put(key, value);
    }

    public void clear() {
        data.clear();
    }

    public ArrayList<NameValuePair> toNameValuePairList() {
        ArrayList<NameValuePair> body = new ArrayList<>();

        Set set = data.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            body.add(new BasicNameValuePair(me.getKey().toString(), me.getValue().toString()));
        }

        return body;
    }
}
