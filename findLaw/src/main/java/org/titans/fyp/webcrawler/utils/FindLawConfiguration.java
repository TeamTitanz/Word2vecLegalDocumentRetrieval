/*******************************************************************************
 * Copyright 2016 Titans
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 ******************************************************************************/
package org.titans.fyp.webcrawler.utils;

import org.apache.log4j.Logger;
import org.titans.fyp.webcrawler.database.DBConnection;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Buddhi on 3/12/2017.
 */
public class FindLawConfiguration {

    final static Logger logger = Logger.getLogger(FindLawConfiguration.class);
    private static Document document;
    private static boolean getFromXml = true;
    private List<String> topicArray;
    private List<String> courtArray;

    public FindLawConfiguration(String[] args) throws ParserConfigurationException, IOException, SAXException {

        if (args.length == 0) {
            ClassLoader classLoader = getClass().getClassLoader();
            File fXmlFile = new File(classLoader.getResource("config.xml").getFile());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(fXmlFile);
        } else {
            topicArray = new ArrayList();
            topicArray.add("cs_" + args[0]);
            DBConnection.setDbCaseName("_" + args[0]);
            logger.info("Law Category: cs_" + args[0]);
            logger.info("Database Name: oblie_" + args[0]);
            DBConnection.setDbUserName(args[1]);
            logger.info("Database UserName: " + args[1]);
            DBConnection.setDbPassword(args[2]);

            getFromXml = false;
        }
    }

    public List<String> getTopicArray() {

        if (getFromXml) {
            topicArray = new ArrayList();
            String lines[] = document.getElementsByTagName("Cases").item(0).getTextContent().split("\\r?\\n");

            for (int i = 0; i < lines.length; i++) {
                String temp = lines[i].replaceAll("\\s+", "");
                if (!temp.equals("")) {
                    topicArray.add(temp);
                }
            }
        }

        return topicArray;
    }

    public List<String> getCourtArray() {

        courtArray = new ArrayList();
        String lines[] = document.getElementsByTagName("Court").item(0).getTextContent().split("\\r?\\n");

        for (int i = 0; i < lines.length; i++) {
            String temp = lines[i].replaceAll("\\s+", "");
            if (!temp.equals("")) {
                courtArray.add(temp);
            }
        }

        return courtArray;
    }
}
