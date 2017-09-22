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

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.titans.fyp.webcrawler.models.Anchor;
import org.titans.fyp.webcrawler.models.Domain;
import org.titans.fyp.webcrawler.models.WebPage;

/**
 * Created by Buddhi on 6/11/2017.
 */
public class test2 {

    public String getReadPage(String searchPage) {
        searchPage = "http://lawcrawler.findlaw.com/LCsearch.html?restrict=lp&client=lp&entry=harvey+v.+veneman";
        try {
            Domain domain = new Domain("http://caselaw.findlaw.com");
            Anchor readAnchor = new Anchor(domain, searchPage);
            WebPage readWebPage = new WebPage(readAnchor);
            readWebPage.getDocumentFromWeb();
            Document readDocument = readWebPage.getDocument();

//            Element readDocementContent =readDocument.select("div.lawcrawler.section").first();
            Elements readDocementContent = readDocument.select("div#gsa_search_app");

            System.out.println(readDocementContent.html());
            for (Element e : readDocementContent) {
                System.out.println(e.html());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        test2 se = new test2();
        se.getReadPage(se.getReadPage(""));
    }


}
