package com.zsxneil.logfilter.simple;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.Set;

public class CustomTest {

    @Test
    public void test() {
        String content = "[{\"name\":\"张三\",\"age\":14,\"phone\":\"17328752127\",\"bankCardNo\":\"17328752127\"}]";
        content = "[{\n" +
                "\t\t\"name\": \"张三\",\n" +
                "\t\t\"age\": 14,\n" +
                "\t\t\"phone\": \"17328752127\",\n" +
                "\t\t\"bankCardNo\": \"17328752127\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"name\": \"张三\",\n" +
                "\t\t\"age\": 14,\n" +
                "\t\t\"phone\": \"17328752127\",\n" +
                "\t\t\"bankCardNo\": \"17328752127\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"name\": \"张三\",\n" +
                "\t\t\"age\": 14,\n" +
                "\t\t\"phone\": \"17328752127\",\n" +
                "\t\t\"bankCardNo\": \"17328752127\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"name\": \"张三\",\n" +
                "\t\t\"age\": 14,\n" +
                "\t\t\"phone\": \"17328752127\",\n" +
                "\t\t\"bankCardNo\": \"17328752127\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"name\": \"张三\",\n" +
                "\t\t\"age\": 14,\n" +
                "\t\t\"phone\": \"17328752127\",\n" +
                "\t\t\"bankCardNo\": \"17328752127\"\n" +
                "\t}\n" +
                "]";


        content = "[{\"name\":\"张三\",\"age\":14,\"phone\":\"17328752127\",\"bankCardNo\":\"17328752127\",\"array\":[{\"name\":\"张三\",\"age\":14,\"phone\":\"17328752127\",\"bankCardNo\":\"17328752127\"}]},{\"name\":\"张三\",\"age\":14,\"phone\":\"17328752127\",\"bankCardNo\":\"17328752127\",\"data\":{\"hello\":\"world\",\"phone\":\"17328752127\"}}]";
        if (content.startsWith("[")) { //jsonArray
            try {
                JSONArray array = JSONArray.parseArray(content);

                iterate(array);
                System.out.println(array.toJSONString());

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                JSONObject jsonObject = JSONObject.parseObject(content);
                iterate(jsonObject);
                System.out.println(jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void iterate(JSONArray array) {
        for (Object item : array) {
            if (item instanceof JSONObject) {
                iterate((JSONObject) item);
            }
        }
    }

    private void iterate(JSONObject jsonObject) {
        Set<String> keySet = jsonObject.keySet();
        for (String key : keySet) {
            if (jsonObject.get(key) instanceof String) {
                // TODO: 2020/11/4  替换打码
                if ("phone".equals(key)) {
                    jsonObject.put(key, jsonObject.getString(key).replace("2875", "****"));
                }
            } else if (jsonObject.get(key) instanceof JSONArray) {
                iterate(jsonObject.getJSONArray(key));
            } else if (jsonObject.get(key) instanceof JSONObject) {
                iterate(jsonObject.getJSONObject(key));
            }
        }
    }

    @Test
    public void jsonTest() {
        String content = "[1,2,3]";
        JSONArray array = JSONArray.parseArray(content);
        for (Object item : array) {
            if (item instanceof Integer || item instanceof Long) {
                System.out.println((int)item);
            }
        }
    }

}
