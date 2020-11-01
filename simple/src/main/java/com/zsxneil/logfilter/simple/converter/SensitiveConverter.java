package com.zsxneil.logfilter.simple.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import static ch.qos.logback.core.util.OptionHelper.extractDefaultReplacement;

public class SensitiveConverter extends MessageConverter {
    //手机号正则匹配
    private static final String PHONE_REGEX = "1[3|4|5|7|8][0-9]\\d{8}";
    //身份证号正则匹配 　　
    private static final String IDCARD_REGEX = "([1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx])|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3})";

    private static final String KEY = "**********";
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
            if(StringUtils.isBlank(content)) {
                return content;
            }
            content = filterIdcard(content);
            return filterMobile(content);
        }catch(Exception e) {
            return content;
        }
    }

    /**
     * [身份证号] 指定展示几位，其他隐藏 。<例子：1101**********5762>
     * @param num 身份证号
     * @return 脱敏后的字符串
     */
    private static String filterIdcard(String num){
        Pattern pattern = Pattern.compile(IDCARD_REGEX);
        Matcher matcher = pattern.matcher(num);
        StringBuffer sb = new StringBuffer() ;
        while(matcher.find()){
            matcher.appendReplacement(sb, baseSensitive(matcher.group(), 4, 4));
        }
        matcher.appendTail(sb) ;
        return sb.toString();
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     * @param num 手机号码
     * @return 脱敏后的字符串
     */
    private static String filterMobile(String num){
        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(num);
        StringBuffer sb = new StringBuffer() ;
        while(matcher.find()){
            matcher.appendReplacement(sb, baseSensitive(matcher.group(), 3, 4)) ;
        }
        matcher.appendTail(sb) ;
        return sb.toString();
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
            char ch;
            if(replacement.charAt(i)>='0' && replacement.charAt(i)<='9') {
                ch = KEY.charAt((int)(replacement.charAt(i) - '0'));
            }else {
                ch = replacement.charAt(i);
            }
            sb.append(ch);
        }
        return StringUtils.left(str, startLength).concat(StringUtils.leftPad(StringUtils.right(str, endLength), str.length() - startLength, sb.toString()));
    }

    /**
     * 按"KEY"中字符解密
     * @param str
     * @param startLength
     * @param endLength
     * @return
     */
    private static String decrypt(String str, int startLength, int endLength) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        String replacement = str.substring(startLength,str.length()-endLength);
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<replacement.length();i++) {
            int index = KEY.indexOf(replacement.charAt(i));
            if(index != -1) {
                sb.append(index);
            }else {
                sb.append(replacement.charAt(i));
            }
        }
        return StringUtils.left(str, startLength).concat(StringUtils.leftPad(StringUtils.right(str, endLength), str.length() - startLength, sb.toString()));
    }

}