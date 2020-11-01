package com.zsxneil.logfilter.stater.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "my.logback.sensitive.converter")
public class SensitiveConverterProperties {

    /**
     * 是否启用转换
     */
    private boolean enable = true;
    /**
     * logback配置文件名称
     */
    private String fileName = "logback-spring.xml";

    private String msgWord = "m";

    /**
     * 手机号码正则表达式
     */
    private String mobileRegex = "1[3|4|5|7|8][0-9]\\d{8}";
    /**
     * 身份证号正则表达式
     */
    private String idRegex = "([1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx])|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3})";


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMobileRegex() {
        return mobileRegex;
    }

    public void setMobileRegex(String mobileRegex) {
        this.mobileRegex = mobileRegex;
    }

    public String getIdRegex() {
        return idRegex;
    }

    public void setIdRegex(String idRegex) {
        this.idRegex = idRegex;
    }

    public String getMsgWord() {
        return msgWord;
    }

    public void setMsgWord(String msgWord) {
        this.msgWord = msgWord;
    }
}
