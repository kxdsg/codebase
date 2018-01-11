package com.argus.xml;


import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;

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
                    if(name.equals("book")) {
                        System.out.println(reader.getAttributeName(0)+":"+reader.getAttributeValue(0));
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
}
