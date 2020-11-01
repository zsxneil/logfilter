package com.zsxneil.logfilter.stater.service;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.zsxneil.logfilter.stater.converter.SensitiveConverter;
import com.zsxneil.logfilter.stater.properties.SensitiveConverterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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

public class LogbackConfigService implements ApplicationRunner {

    private Logger log = LoggerFactory.getLogger("logfilter");

    private SensitiveConverterProperties sensitiveConverterProperties;

    public LogbackConfigService(SensitiveConverterProperties sensitiveConverterProperties) {

        this.sensitiveConverterProperties = sensitiveConverterProperties;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 设置转换器的正则表达式
        SensitiveConverter.IDCARD_REGEX = sensitiveConverterProperties.getIdRegex();
        SensitiveConverter.PHONE_REGEX = sensitiveConverterProperties.getMobileRegex();

        if (sensitiveConverterProperties.isEnable()) {
            String configFilePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + sensitiveConverterProperties.getFileName();
            log.info(configFilePath);

            OutputStream outputStream = null;
            InputStream inputStream = null;
            try {
                //得到DOM解析器的工厂实例
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                //从DOM工厂中获得DOM解析器
                DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
                //把要解析的xml文档读入DOM解析器
                Document doc = dbBuilder.parse(configFilePath);
                //得到文档名称为Student的元素的节点列表
                NodeList nList = doc.getElementsByTagName("configuration");
                Element configuration = (Element)nList.item(0);

                Element conversionRule = doc.createElement("conversionRule");
                //设置元素Student的属性值为231
                conversionRule.setAttribute("conversionWord", sensitiveConverterProperties.getMsgWord());
                conversionRule.setAttribute("converterClass", SensitiveConverter.class.getName());

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
                context.reset();
                configurator.doConfigure(inputStream);
            } catch (Exception e) {
                log.error("logfilter config failed:{}", e.getLocalizedMessage(), e);
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
