package com.test.pdf_craft.utils;

import com.google.gson.Gson;

/**
 * @Author: xiongchaoyu
 * @CreateTime: 2025-02-21  10:46
 * @Description:
 * @Version: 1.0
 */
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
