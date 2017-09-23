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

package org.titans.fyp.webcrawler;


import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.titans.fyp.webcrawler.DAO.CaseController;
import org.titans.fyp.webcrawler.models.*;

import java.util.ArrayList;

/**
 * Information extraction process on the summary and the read page happens here.
 */
public class PageCollector {

    static String domainURL;
    final static Logger logger = Logger.getLogger(PageCollector.class);

    public static void setDomain(String domain) {
        domainURL = domain;
    }

    public static void Crawl(String readPageURL) {

        try {
            CaseInfo lawCase = new CaseInfo();
            ArrayList<Case> CaseContent = new ArrayList<Case>();
            Domain domain = new Domain(domainURL);
            int caseIndex = Initiator.caseID;

            //------start extracting HTML document from Read page-------
            Anchor readAnchor = new Anchor(domain, readPageURL);
            WebPage readWebPage = new WebPage(readAnchor);
            readWebPage.getDocumentFromWeb();
            Document readDocument = readWebPage.getDocument();

//            System.out.println(readDocument);
            lawCase.setCaseId(caseIndex);
            lawCase.setName(readDocument.select("h1").text());
            lawCase.setRealName(readDocument.select("center").select("h3").text());
            lawCase.setSummaryPageURL("");
            lawCase.setReadPageURL(readPageURL);

            Elements readDocementContent = readDocument.select
                    (".caselawcontent .searchable-content").first().children();
            StringBuilder sb = new StringBuilder();
            for (Element e : readDocementContent) {
                for (Element e1 : e.select("a[href]")) {
                    String mention_url = e1.attr("href");
                    if (mention_url.contains(".html")) {
                        sb.append(mention_url);
                        sb.append(" , ");
                    }
                }

                String extractedText = e.text();
                CaseContent.add(new Case(caseIndex, extractedText));
//                System.out.println(extractedText);
            }
            lawCase.setMentionCasesUrl(sb.toString());
//            System.out.println(sb.toString());
            //------end extracting HTML document from Read page-------

//            logger.info(caseName + ", " + summaryPageURL + ", " + readPageURL);
            CaseController.addCase(lawCase, CaseContent);
            Initiator.caseID++;

        } catch (Exception e) {
            logger.error("ID: " + (Initiator.caseID-588) + "\tReadPage:" + readPageURL + "\n" + e.getMessage());
        }
    }

}
