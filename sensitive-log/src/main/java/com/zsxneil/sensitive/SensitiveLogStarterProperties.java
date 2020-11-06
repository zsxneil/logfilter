package com.zsxneil.sensitive;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 敏感日志打码配置类
 *
 * @author
 * @version v1.0
 * @date 2020-11-06 20:18
 **/

@ConfigurationProperties(prefix = "common.sensitive")
public class SensitiveLogStarterProperties {
    /**
     * 是否启用，默认为 启用
     **/
    private boolean enable = true;
    /**
     * logback配置中消息体的标识
     **/
    private String msgWord = "m";
    /**
     * logback配置 文件名
     **/
    private String configFilename = "logback-spring.xml";
    /**
     * 日志前缀标识，建议配置，可以提高效率
     **/
    private String msgPrefix = "";

    /**
     * 手机号字段配置
     **/
    private Phone phone = new Phone();
    /**
     * 姓名字段配置
     **/
    private Name name = new Name();
    /**
     * 证件号字段配置
     **/
    private IdCardNo idCardNo = new IdCardNo();
    /**
     * 银行卡号字段配置
     **/
    private BankCardNo bankCardNo = new BankCardNo();


    public static class Phone {
        /**
         * 是否启用
         **/
        private boolean enable = true;
        /**
         * 字段名，多个用,隔开
         **/
        private String fields = "phone";

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getFields() {
            return fields;
        }

        public void setFields(String fields) {
            this.fields = fields;
        }
    }

    public static class Name {
        /**
         * 是否启用
         **/
        private boolean enable = true;
        /**
         * 字段名，多个用,隔开
         **/
        private String fields = "name";

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getFields() {
            return fields;
        }

        public void setFields(String fields) {
            this.fields = fields;
        }
    }

    public static class IdCardNo {
        /**
         * 是否启用
         **/
        private boolean enable = true;
        /**
         * 字段名，多个用,隔开
         **/
        private String fields = "idCardNo";

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getFields() {
            return fields;
        }

        public void setFields(String fields) {
            this.fields = fields;
        }
    }

    public static class BankCardNo {
        /**
         * 是否启用
         **/
        private boolean enable = true;
        /**
         * 字段名，多个用,隔开
         **/
        private String fields = "bankCardNo";

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getFields() {
            return fields;
        }

        public void setFields(String fields) {
            this.fields = fields;
        }
    }


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getMsgWord() {
        return msgWord;
    }

    public void setMsgWord(String msgWord) {
        this.msgWord = msgWord;
    }

    public String getConfigFilename() {
        return configFilename;
    }

    public void setConfigFilename(String configFilename) {
        this.configFilename = configFilename;
    }

    public String getMsgPrefix() {
        return msgPrefix;
    }

    public void setMsgPrefix(String msgPrefix) {
        this.msgPrefix = msgPrefix;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public IdCardNo getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(IdCardNo idCardNo) {
        this.idCardNo = idCardNo;
    }

    public BankCardNo getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(BankCardNo bankCardNo) {
        this.bankCardNo = bankCardNo;
    }
}

