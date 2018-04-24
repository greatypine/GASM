// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlUtil.java

package com.cnpc.pms.base.util;

import com.cnpc.pms.base.exception.UtilityException;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.apache.commons.betwixt.io.BeanWriter;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtil
{

    public XmlUtil()
    {
    }

    public String beanToXml(Object o)
        throws Exception
    {
        StringWriter stringWriter = new StringWriter();
        BeanWriter beanWriter = new BeanWriter(stringWriter);
        beanWriter.write(o);
        String xmlData = stringWriter.toString();
        stringWriter.close();
        return xmlData;
    }

    public static NodeList getNodesByXpath(InputStream in, String xpathString)
        throws IOException, UtilityException
    {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        org.w3c.dom.Document document = null;
        try
        {
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            document = builder.parse(in);
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr = xpath.compile(xpathString);
            Object result = expr.evaluate(document, XPathConstants.NODESET);
            NodeList nodes = (NodeList)result;
            return nodes;
        }
        catch(ParserConfigurationException e)
        {
            throw new UtilityException((new StringBuilder()).append("Fail to parse xpath: ").append(xpathString).toString(), e);
        }
        catch(SAXException e)
        {
            throw new UtilityException((new StringBuilder()).append("Fail to parse xpath: ").append(xpathString).toString(), e);
        }
        catch(XPathExpressionException e)
        {
            throw new UtilityException((new StringBuilder()).append("Fail to parse xpath: ").append(xpathString).toString(), e);
        }
    }
}
