package com.zsxneil.sensitive;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.zsxneil.sensitive.converter.JsonSensitiveConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 日志敏感信息处理类
 *
 * @author
 * @version v1.0
 * @date 2020-11-06 20:53
 **/
@Order(1)
public class SensitiveLogService implements ApplicationRunner {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private SensitiveLogStarterProperties sensitiveLogStarterProperties;

    public SensitiveLogService(SensitiveLogStarterProperties sensitiveLogStarterProperties) {
        this.sensitiveLogStarterProperties = sensitiveLogStarterProperties;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 配置项
        JsonSensitiveConverter.properties = sensitiveLogStarterProperties;
        if (StringUtils.isNotBlank(sensitiveLogStarterProperties.getPhone().getFields())) {
            JsonSensitiveConverter.phoneFieldList = Arrays.asList(sensitiveLogStarterProperties.getPhone().getFields().split(","));
        }
        if (StringUtils.isNotBlank(sensitiveLogStarterProperties.getName().getFields())) {
            JsonSensitiveConverter.nameFieldList = Arrays.asList(sensitiveLogStarterProperties.getName().getFields().split(","));
        }
        if (StringUtils.isNotBlank(sensitiveLogStarterProperties.getIdCardNo().getFields())) {
            JsonSensitiveConverter.idCardNoFieldList = Arrays.asList(sensitiveLogStarterProperties.getIdCardNo().getFields().split(","));
        }
        if (StringUtils.isNotBlank(sensitiveLogStarterProperties.getBankCardNo().getFields())) {
            JsonSensitiveConverter.bankCardNoList = Arrays.asList(sensitiveLogStarterProperties.getBankCardNo().getFields().split(","));
        }
        JsonSensitiveConverter.msgPrefix = sensitiveLogStarterProperties.getMsgPrefix();

        if (sensitiveLogStarterProperties.isEnable()) {
            log.info("sensitiveLog-starter is enable");

            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(sensitiveLogStarterProperties.getConfigFilename());
            Resource resource = resources[0];
            log.debug("logback config filePath: {}", resource.getURL().toString());

            OutputStream outputStream = null;
            InputStream inputStream = null;
            try {
                //得到DOM解析器的工厂实例
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                //从DOM工厂中获得DOM解析器
                DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
                //把要解析的xml文档读入DOM解析器
                Document doc = dbBuilder.parse(resource.getInputStream());
                //得到文档名称为configuration的元素的节点列表
                NodeList nList = doc.getElementsByTagName("configuration");
                Element configuration = (Element)nList.item(0);

                Element conversionRule = doc.createElement("conversionRule");
                //设置元素conversionRule的conversionWord和converterClass属性
                conversionRule.setAttribute("conversionWord", sensitiveLogStarterProperties.getMsgWord());
                conversionRule.setAttribute("converterClass", JsonSensitiveConverter.class.getName());
                //将元素conversionRule插入到最前面
                configuration.insertBefore(conversionRule, configuration.getFirstChild());

                TransformerFactory transformerFactory =
                        TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);

                outputStream = new ByteArrayOutputStream();
                StreamResult memoryResult = new StreamResult(outputStream);
                transformer.transform(source, memoryResult);
                inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());

                LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(context);
                Map<String, String> copyOfPropertyMap = context.getCopyOfPropertyMap();

                context.reset();
                //将日志文件中配置的属性再put进去
                Set<String> keySet = copyOfPropertyMap.keySet();
                for (String key : keySet) {
                    context.putProperty(key, copyOfPropertyMap.get(key));
                }

                configurator.doConfigure(inputStream);
                log.info("sensitiveLog-starter init finished");
            } catch (Exception e) {
                log.error("sensitiveLog-starter init failed:{}", e.getLocalizedMessage(), e);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }

        }
    }
}

