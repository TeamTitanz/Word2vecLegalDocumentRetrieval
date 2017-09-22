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

import java.util.ArrayList;
import java.util.List;


public final class QueryBuilder {

    private WebPage webpage;

    private final List<String> courtArray;
    private final List<String> topicArray;
    private final List<String> queryArray;
    private final List<Pagination> paginationArray;

    public List<String> getQueryArray() {
        return queryArray;
    }

    public QueryBuilder(WebPage webpage, List<String> topicArray) throws Exception {

        this.webpage = webpage;
        this.topicArray = topicArray;
        courtArray = new ArrayList();
        queryArray = new ArrayList();
        paginationArray = new ArrayList();

        createCourtList(webpage);
        createQueryList();
        getPaginatedUrlList();
    }

    public List<Pagination> getPaginationArray() {
        return paginationArray;
    }

    public WebPage getWebpage() {
        return webpage;
    }

    public List<String> getCourtArray() {
        return courtArray;
    }

    public List<String> getTopicArray() {
        return topicArray;
    }

    public void getPaginatedUrlList() throws Exception {

        for (String url : queryArray) {

            Domain domain = new Domain(url);
            Anchor anchor = new Anchor(domain, url);
            WebPage webPage = new WebPage(anchor);
            webPage.getDocumentFromWeb();

            Pagination pagination = new Pagination(webPage);
            paginationArray.add(pagination);

        }
    }

    private void createCourtList(WebPage webpage) {
        String courtSelect = webpage.getDocument().getElementById("court").toString();
        Document document = Jsoup.parse(courtSelect);
        Elements options = document.select("select > option");

        for (Element element : options) {
            if (!element.attr("value").isEmpty()) {
                courtArray.add(element.attr("value"));
            }

        }
    }

    private void createTopicList(WebPage webpage) {
        String topicSelect = webpage.getDocument().getElementById("topic").toString();
        Document document = Jsoup.parse(topicSelect);
        Elements options = document.select("select > option");

        for (Element element : options) {
            if (!element.attr("value").isEmpty()) {
                topicArray.add(element.attr("value"));
            }

        }
    }

    private void createQueryList() {
        String domainUrl = "http://caselaw.findlaw.com/summary/search";

        for (String court : courtArray) {
            for (String topic : topicArray) {

                String query = domainUrl + "?" + "court="
                        + court + "&" + "topic="
                        + topic;
                queryArray.add(query);

            }
        }
    }

    public List getPaginatedURLs() {
        return paginationArray;
    }

}