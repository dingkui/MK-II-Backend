package com.mileworks.gen.common.domain;

import java.util.HashMap;

public class MKResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;

    public MKResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public MKResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    @Override
    public MKResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
