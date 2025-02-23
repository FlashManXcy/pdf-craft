package com.test.pdf_craft.utils;

import com.google.gson.Gson;


public class JSONUtils {
    /**
     * bean to json
     */
    public static String beanToJson(Object obj) {
        if (obj == null) {
            return null;
        }
        // Bean -> Json
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        return json;
    }
}
