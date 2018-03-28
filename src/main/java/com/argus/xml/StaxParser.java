package com.argus.xml;


import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Created by xingding on 18/1/10.
 */
public class StaxParser {



    @Test
    public void readBookAttr(){
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream is = null;
        try {
            is = StaxParser.class.getClassLoader().getResourceAsStream("books.xml");
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            while(reader.hasNext()) {
                int type = reader.next();
                if(type==XMLStreamConstants.START_ELEMENT) {
                    String name = reader.getName().toString();
                    //获取属性
                    if(name.equals("book")) {
                        System.out.println(reader.getAttributeName(0)+":"+reader.getAttributeValue(0));
                    }
                    //获取节点文本
                    if(name.equals("title")){
                        System.out.println(reader.getElementText());
                    }
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } finally {
            try {
                if(is!=null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    /**
     * 使用XMLEvent来读取title和price标签的值
     * <title lang="en">Everyday Italian</title>
     * <price>30.00</price>
     */
    public void test04() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream is = null;
        try {
            is = StaxParser.class.getClassLoader().getResourceAsStream("books.xml");
            //基于迭代模型的操作方式
            XMLEventReader reader = factory.createXMLEventReader(is);
            int num = 0;
            while(reader.hasNext()) {
                //通过XMLEvent来获取是否是某种节点类型
                XMLEvent event = reader.nextEvent();
                if(event.isStartElement()) {
                    //通过event.asxxx转换节点
                    String name = event.asStartElement().getName().toString();
                    if(name.equals("title")) {
                        System.out.print(reader.getElementText()+":");
                    }
                    if(name.equals("price")) {
                        System.out.print(reader.getElementText()+"\n");
                    }
                }
                num++;
            }
            System.out.println(num);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } finally {
            try {
                if(is!=null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    /**
     * 使用Filter过滤的不需要操作的元素，提高效率
     * 同时使用XMLEvent
     */
    public void test05() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream is = null;
        try {
            is = StaxParser.class.getClassLoader().getResourceAsStream("books.xml");
            //基于Filter的过滤方式，可以有效的过滤掉不用进行操作的节点，效率会高一些
            XMLEventReader reader = factory.createFilteredReader(factory.createXMLEventReader(is),
                    new EventFilter() {
                        @Override
                        public boolean accept(XMLEvent event) {
                            //返回true表示会显示，返回false表示不显示
                            if(event.isStartElement()) {
                                String name = event.asStartElement().getName().toString();
                                if(name.equals("title")||name.equals("price"))
                                    return true;
                            }
                            return false;
                        }
                    });
            int num = 0;
            while(reader.hasNext()) {
                //通过XMLEvent来获取是否是某种节点类型
                XMLEvent event = reader.nextEvent();
                if(event.isStartElement()) {
                    //通过event.asxxx转换节点
                    String name = event.asStartElement().getName().toString();
                    if(name.equals("title")) {
                        System.out.print(reader.getElementText()+":");
                    }
                    if(name.equals("price")) {
                        System.out.print(reader.getElementText()+"\n");
                    }
                }
                num++;
            }
            System.out.println(num);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } finally {
            try {
                if(is!=null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    /**
     * 使用XPath和表达式精确定位元素(基于document的形式，在文档很大的情况下效率差)
     * 表达式：//book[@category='WEB']
     * 意义：查找book元素的属性等于WEB的节点
     */
    public void test06() {
        InputStream is = null;
        try {
            is = StaxParser.class.getClassLoader().getResourceAsStream("books.xml");
            //创建文档处理对象
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            //通过DocumentBuilder创建doc的文档对象
            Document doc = db.parse(is);
            //创建XPath
            XPath xpath = XPathFactory.newInstance().newXPath();
            //第一个参数就是xpath,第二参数就是文档
            NodeList list = (NodeList)xpath.evaluate("//book[@category='WEB']", doc, XPathConstants.NODESET);
            for(int i=0;i<list.getLength();i++) {
                //遍历输出相应的结果
                Element e = (Element)list.item(i);
                System.out.println(e.getElementsByTagName("title").item(0).getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is!=null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    /**
     * 使用XMLStreamWriter生成XML
     */
    public void test07() {
        try {
            XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(System.out);
            xsw.writeStartDocument("UTF-8","1.0");
            xsw.writeEndDocument();
            String ns = "http://11:dd";
            xsw.writeStartElement("person");
            xsw.writeAttribute("name", "张三");
            xsw.writeStartElement("age");
            xsw.writeCharacters("1");
            xsw.writeEndElement();
            xsw.writeEndElement();
            xsw.flush();
            xsw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * 使用Xpath定位元素并通过Transformer修改其中元素的值
     */
    public void test08() {
        InputStream is = null;
        try {
            is = StaxParser.class.getClassLoader().getResourceAsStream("books.xml");
            //创建文档处理对象
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            //通过DocumentBuilder创建doc的文档对象
            Document doc = db.parse(is);
            //创建XPath
            XPath xpath = XPathFactory.newInstance().newXPath();
            Transformer tran = TransformerFactory.newInstance().newTransformer();
            tran.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
            tran.setOutputProperty(OutputKeys.INDENT, "yes");
            //第一个参数就是xpath,第二参数就是文档
            NodeList list = (NodeList)xpath.evaluate("//book[title='Learning XML']", doc,XPathConstants.NODESET);
            //获取price节点
            Element be = (Element)list.item(0);
            Element e = (Element)(be.getElementsByTagName("price").item(0));
            e.setTextContent("333.9");
            Result result = new StreamResult(System.out);
            //通过tranformer修改节点
            tran.transform(new DOMSource(doc), result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is!=null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
