package com.zsxneil.logfilter.simple;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zsxneil.logfilter.simple.util.FindJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
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

    @Test
    public void findJsonTest() {
        String content = "[{\"name\":\"张三\",\"age\":14,\"phone\":\"17328752127\",\"bankCardNo\":\"17328752127\",\"array\":[{\"name\":\"张三\",\"age\":14,\"phone\":\"17328752127\",\"bankCardNo\":\"17328752127\"}]},{\"name\":\"张三\",\"age\":14,\"phone\":\"17328752127\",\"bankCardNo\":\"17328752127\",\"data\":{\"hello\":\"world\",\"phone\":\"17328752127\"}}]";
        content = "[{\"name\":\"张三\",\"age\":14,\"phone\":\"17328752127\",\"bankCardNo\":\"17328752127\",\"array\":[{\"name\":\"张三\",\"age\":14,\"phone\":\"17328752127\",\"bankCardNo\":\"17328752127\"}]},{\"name\":\"张三\",\"age\":14,\"phone\":\"17328752127\",\"bankCardNo\":\"17328752127\",\"data\":{\"hello\":\"world\",\"phone\":\"17328752127\"}}],msg={\"type\":1,\"status\":\"SUCCESS\"}";
        content = "[{\n" +
                "\t\"name\": \"张三\",\n" +
                "\t\"age\": 14,\n" +
                "\t\"phone\": \"17328752127\",\n" +
                "\t\"bankCardNo\": \"17328752127\",\n" +
                "\t\"array\": [{\n" +
                "\t\t\"name\": \"张三\",\n" +
                "\t\t\"age\": 14,\n" +
                "\t\t\"phone\": \"17328752127\",\n" +
                "\t\t\"bankCardNo\": \"17328752127\"\n" +
                "\t}]\n" +
                "}, {\n" +
                "\t\"name\": \"张三\",\n" +
                "\t\"age\": 14,\n" +
                "\t\"phone\": \"17328752127\",\n" +
                "\t\"bankCardNo\": \"17328752127\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"hello\": \"world\",\n" +
                "\t\t\"phone\": \"17328752127\"\n" +
                "\t}\n" +
                "}], msg = {\n" +
                "\t\"type\": 1,\n" +
                "\t\"status\": \"SUCCESS\"\n" +
                "}";

        List<FindJsonUtil.JsonInfo> jsonList = FindJsonUtil.format(content);

//        System.out.println(content.substring(279, 308));

        if (jsonList != null && jsonList.size() > 0) {
            //倒序排列，从后往前替换脱敏后的字符串
            Collections.sort(jsonList, ((o1, o2) -> o2.getStartIndex() - o1.getStartIndex()));
            for (FindJsonUtil.JsonInfo jsonInfo : jsonList) {
                String retJsonString = jsonInfo.getJsonString() + "***";
                content = new StringBuffer(content.substring(0, jsonInfo.getStartIndex()))
                        .append(retJsonString)
                        .append(content.substring(jsonInfo.getEndIndex()))
                        .toString();
            }
        }

        System.out.println(content);

    }
}
