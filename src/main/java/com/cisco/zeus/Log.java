package com.cisco.zeus;

import org.json.JSONObject;

import java.io.IOException;


/**
 * Log Object
 * <p>
 * A pair of (string, Object)
 */
public class Log {
    private boolean mutable;
    public JSONObject data;

    public Log() {
        mutable = true;
        data = new JSONObject();
    }

    public Log setKeyValues(String key, Object value) throws IOException {
        if (!mutable) {
            throw new IOException("Log object is immutable");
        }

        data.put(key, value);
        return this;
    }

    public Log build() {
        mutable = false;

        return this;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
