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
package org.titans.fyp.webcrawler.findLaw;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.titans.fyp.webcrawler.models.Anchor;
import org.titans.fyp.webcrawler.models.Domain;
import org.titans.fyp.webcrawler.models.WebPage;

import java.util.HashMap;
import java.util.Map;


public class PaginatedPageCaseSet {

    private WebPage webPage;
    private Map<String, String> caseMap;

    public Map<String, String> getCaseMap() {
        return caseMap;
    }

    public PaginatedPageCaseSet(WebPage webPage) throws Exception {

        this.webPage = webPage;

        caseMap = new HashMap<String, String>();

        createCaseList(webPage);
        setDetailedCaseLawUrl();

    }

    private void setDetailedCaseLawUrl() throws Exception {

        for (Map.Entry<String, String> caseEntry : caseMap.entrySet()) {
            String caseURL = caseEntry.getKey();
            Domain domain = new Domain(caseURL);
            Anchor anchor = new Anchor(domain, caseURL);
            WebPage webPage = new WebPage(anchor);
            webPage.getDocumentFromWeb();

            String detailedCaseLawUrl = getDetailedCaseLawUrl(webPage);
            caseEntry.setValue(detailedCaseLawUrl);
        }

    }

    private String getDetailedCaseLawUrl(WebPage webPage) {

        String buttonText = webPage.getDocument().getElementsByClass("btn_read").toString();

        Document document = Jsoup.parse(buttonText);
        Elements options = document.select("a[href]");

        String link = "";

        for (Element element : options) {

            link = (element.attr("href"));

        }
        return link;

    }

    private void createCaseList(WebPage webPage) {
        String caseLawTable = webPage.getDocument().getElementById("srpcaselaw").toString();

        Document document = Jsoup.parse(caseLawTable);
        Elements options = document.select("a[href]");

        for (Element element : options) {

            caseMap.put(element.attr("href"), null);

        }
    }

}
