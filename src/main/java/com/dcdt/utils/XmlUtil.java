package com.dcdt.utils;

/**
 * Created by wtwang on 2018/3/17.
 */
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.*;

/**
 * User: Lu Tingming
 * Time: 2010-11-11 20:53:54
 * Desc: XML工具
 */
public class XmlUtil {
    /**
     * 读入XML文件，返回Document对象
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static Document readDocument(String path) throws Exception {
        FileInputStream reader = null;

        try {
            reader = new FileInputStream(path);
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(reader);

            return doc;
        } catch (Exception e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    throw e1;
                }
            }
        }
    }

    /**
     * added by rocwu 2016/10/27
     * @param str XML字符串
     * @return DOM对象
     * @throws Exception
     */
    public static Document readDocumentFromStr(String str) throws Exception {
        FileInputStream reader = null;

        try {
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(str));
            SAXBuilder sb = new SAXBuilder();
            return sb.build(is);
        } catch (Exception e) {
            throw e;
        }
    }

}
