package com.zsxneil.sensitive.converter;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.fastjson.parser.Feature;
import com.zsxneil.sensitive.SensitiveLogStarterProperties;
import com.zsxneil.sensitive.util.FindJsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * json敏感日志转换器
 *
 * @author
 * @version v1.0
 * @date 2020-11-06 20:47
 **/
public class JsonSensitiveConverter extends MessageConverter {

    public static SensitiveLogStarterProperties properties;

    public static String msgPrefix = "";

    /**
     * 手机号码的key列表
     **/
    public static List<String> phoneFieldList = new ArrayList<>();
    /**
     * 姓名的key列表
     **/
    public static List<String> nameFieldList = new ArrayList<>();
    /**
     * 证件号码的key列表
     **/
    public static List<String> idCardNoFieldList = new ArrayList<>();
    /**
     * 银行卡号的key列表
     **/
    public static List<String> bankCardNoList = new ArrayList<>();


    private static final String KEY = "*";
    @Override
    public String convert(ILoggingEvent event){

        // 获取原始日志
        String requestLogMsg = event.getFormattedMessage();

        // 获取返回脱敏后的日志
        return filterSensitive(requestLogMsg);
    }

    /**
     * 对敏感信息脱敏
     * @param content 需要脱敏的字符串
     * @return 脱敏后的字符串
     */
    public static String filterSensitive(String content) {
        try {
            // 如果配置了日志前缀，则只转换有日志前缀的；否则，全部转换
            if (StringUtils.isNotBlank(msgPrefix) && !content.startsWith(msgPrefix)) {
                return content;
            }
            List<String> jsonList = FindJsonUtil.format(content);

            for (String line : jsonList) {
                content = content.replace(line, filterJson(line));
            }
        }catch(Exception e) {
        }
        return content;
    }


    private static String filterJson(String line) {
        JSONValidator validator = JSONValidator.from(line);
        if (validator.validate()) {
            if (validator.getType() == JSONValidator.Type.Array) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("{\"prefixKey\":").append(line).append("}");
                JSONObject jsonObject = JSONObject.parseObject(buffer.toString(), Feature.OrderedField);
                iterate(jsonObject);
                JSONArray array = jsonObject.getJSONArray("prefixKey");
                return array.toJSONString();
            } else if (validator.getType() == JSONValidator.Type.Object) {
                JSONObject jsonObject = JSONObject.parseObject(line, Feature.OrderedField);
                iterate(jsonObject);
                return jsonObject.toJSONString();
            }
        }
        return line;
    }

    /**
     * 遍历jsonArray
     **/
    private static void iterate(JSONArray array) {
        for (Object item : array) {
            if (item instanceof JSONObject) {
                iterate((JSONObject) item);
            }
        }
    }

    /**
     * 遍历jsonObject
     **/
    private static void iterate(JSONObject jsonObject) {
        Set<String> keySet = jsonObject.keySet();
        for (String key : keySet) {
            if (jsonObject.get(key) instanceof String) {

                // 按手机号、银行卡号、证件号、姓名分别分类打码
                if (properties.getPhone().isEnable() && phoneFieldList.contains(key)) {
                    // 手机号码打码
                    if (StringUtils.isNotBlank(jsonObject.getString(key)) && jsonObject.getString(key).length() == 11) {
                        jsonObject.put(key, baseSensitive(jsonObject.getString(key), 3, 2));
                    }
                } else if (properties.getName().isEnable() && nameFieldList.contains(key)) {
                    // 姓名打码
                    if (StringUtils.isNotBlank(jsonObject.getString(key)) && jsonObject.getString(key).length() > 1) {
                        jsonObject.put(key, baseSensitive(jsonObject.getString(key), 1, 0));
                    }
                } else if (properties.getBankCardNo().isEnable() && bankCardNoList.contains(key)) {
                    // 银行卡号打码
                    if (StringUtils.isNotBlank(jsonObject.getString(key)) && jsonObject.getString(key).length() >= 9) {
                        jsonObject.put(key, baseSensitive(jsonObject.getString(key), 4, 4));
                    }
                } else if (properties.getIdCardNo().isEnable() && idCardNoFieldList.contains(key)) {
                    // 证件号打码
                    if (StringUtils.isNotBlank(jsonObject.getString(key)) && jsonObject.getString(key).length() == 18) {
                        jsonObject.put(key, baseSensitive(jsonObject.getString(key), 4, 0));
                    }
                } else {
                    JSONValidator validator = JSONValidator.from(jsonObject.getString(key));
                    if (validator.validate()) {
                        jsonObject.put(key, filterJson(jsonObject.getString(key)));
                    }
                }

            } else if (jsonObject.get(key) instanceof JSONArray) {
                iterate(jsonObject.getJSONArray(key));
            } else if (jsonObject.get(key) instanceof JSONObject) {
                iterate(jsonObject.getJSONObject(key));
            }
        }
    }

    /**
     * 基础脱敏处理 指定起止展示长度 剩余用"KEY"中字符替换
     *
     * @param str         待脱敏的字符串
     * @param startLength 开始展示长度
     * @param endLength   末尾展示长度
     * @return 脱敏后的字符串
     */
    private static String baseSensitive(String str, int startLength, int endLength) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        String replacement = str.substring(startLength,str.length()-endLength);
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<replacement.length();i++) {
            sb.append(KEY);
        }
        return StringUtils.left(str, startLength).concat(StringUtils.leftPad(StringUtils.right(str, endLength), str.length() - startLength, sb.toString()));
    }


}

