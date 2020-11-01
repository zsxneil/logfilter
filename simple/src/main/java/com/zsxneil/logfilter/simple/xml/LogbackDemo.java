package com.zsxneil.logfilter.simple.xml;

import org.springframework.util.StreamUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class LogbackDemo {


    /**
     * 向已存在的xml文件中插入元素
     * */
    public static InputStream insertXml(){
        Element configuration = null;
        Element conversionRule = null;
        Element name = null;
        Element num = null;
        Element classes = null;
        Element address = null;
        Element tel = null;
        try{
            //得到DOM解析器的工厂实例
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            //从DOM工厂中获得DOM解析器
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            //把要解析的xml文档读入DOM解析器
            Document doc = dbBuilder.parse("E:/web/idea-workspace/log-filter/simple/src/main/resources/logback-spring.xml");
            //得到文档名称为Student的元素的节点列表
            NodeList nList = doc.getElementsByTagName("configuration");
            configuration = (Element)nList.item(0);

            conversionRule = doc.createElement("conversionRule");
            //设置元素Student的属性值为231
            conversionRule.setAttribute("conversionWord", "m");
            conversionRule.setAttribute("converterClass", "com.zsxneil.logfilter.simple.converter.SensitiveConverter");

            configuration.insertBefore(conversionRule, configuration.getFirstChild());
            //将内存中的文档通过文件流生成insertSchool.xml,XmlDocument位于crison.jar下

//            ((XmlDocument)doc).write(new FileOutputStream("src/xidian/sl/dom/insertSchool.xml"));


            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            System.out.println("-----------Modified File-----------");
//            StreamResult streamResult = new StreamResult(new FileOutputStream("E:/web/idea-workspace/log-filter/simple/src/main/resources/logback-spring_new.xml"));
//            transformer.transform(source, streamResult);

            OutputStream outputStream = new ByteArrayOutputStream();
            StreamResult memoryResult = new StreamResult(outputStream);
            transformer.transform(source, memoryResult);

            InputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());
            System.out.println("成功");
            return inputStream;


        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args){
        //插入
        LogbackDemo.insertXml();
    }

}
