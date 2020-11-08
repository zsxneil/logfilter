package com.zsxneil.logfilter.simple.util;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * Created by neil.zhang on 2020/11/08.
 */
public class FindJsonUtil {


    public static List<JsonInfo> format(String jsonStr) {
        Stack<JsonStack> stringStack = new Stack<JsonStack>();
        List<Integer> indexList = new LinkedList<Integer>();
        List<JsonInfo> jsonList = new ArrayList<JsonInfo>();
        for (int i = 0; i < jsonStr.length(); i++) {
            if (jsonStr.charAt(i) == '{' || jsonStr.charAt(i) == '[') {
                stringStack.push(new JsonStack(i, jsonStr.charAt(i)));
            } else if (jsonStr.charAt(i) == '}' || jsonStr.charAt(i) == ']') {
                if (!stringStack.empty()) {
                    JsonStack js = stringStack.peek();
                    if (jsonStr.charAt(i) == '}' && js.getStr() == '{') {
                        js = stringStack.pop();
                    } else if (jsonStr.charAt(i) == ']' && js.getStr() == '[') {
                        js = stringStack.pop();
                    }
                    indexList.add(js.getIndex());
                    indexList.add(i);
                }
                if (stringStack.empty()) {
                    JsonInfo jsonInfo = getJsonStr(indexList, jsonStr);
                    if (Objects.nonNull(jsonInfo)) {
                        jsonList.add(jsonInfo);
                    }
                    indexList.clear();
                }
            }
        }
        if (indexList != null && indexList.size() > 0) {
            JsonInfo jsonInfo = getJsonStr(indexList, jsonStr);
            if (Objects.nonNull(jsonInfo)) {
                jsonList.add(jsonInfo);
            }
        }
        return  jsonList;
    }

    private static JsonInfo getJsonStr(List<Integer> indexList,String str) {
        String temp = "";
        for (int i = indexList.size() - 1; i >= 0; i = i - 2) {
            try {
                temp = str.substring(indexList.get(i - 1), indexList.get(i) + 1);
                JSON.parse(temp);
                return new JsonInfo(indexList.get(i -1), indexList.get(i) + 1, str.substring(indexList.get(i - 1), indexList.get(i) + 1));
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }




    static class JsonStack{
        private Integer index;
        private char str;

        public JsonStack(Integer index, char str) {
            this.index = index;
            this.str = str;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public Character getStr() {
            return str;
        }

        public void setStr(Character str) {
            this.str = str;
        }
    }

    public static class JsonInfo {
        private Integer startIndex;
        private Integer endIndex;
        private String jsonString;

        public JsonInfo(Integer startIndex, Integer endIndex, String jsonString) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.jsonString = jsonString;
        }

        public Integer getStartIndex() {
            return startIndex;
        }

        public void setStartIndex(Integer startIndex) {
            this.startIndex = startIndex;
        }

        public Integer getEndIndex() {
            return endIndex;
        }

        public void setEndIndex(Integer endIndex) {
            this.endIndex = endIndex;
        }

        public String getJsonString() {
            return jsonString;
        }

        public void setJsonString(String jsonString) {
            this.jsonString = jsonString;
        }
    }
}

