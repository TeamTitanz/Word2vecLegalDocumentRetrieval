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


public class Pagination {

    private WebPage webPage;
    private List<String> pagesList;
    private List<PaginatedPageCaseSet> paginatedPageCaseSets;
    private int pageCount = 1;

    public Pagination(WebPage webPage) throws Exception {

        this.webPage = webPage;

        pagesList = new ArrayList<String>();
        paginatedPageCaseSets = new ArrayList<PaginatedPageCaseSet>();

        createPageList(webPage);

        getCaseSetUrls();

    }

    public void getCaseSetUrls() throws Exception {

        for (String url : pagesList) {
            Domain domain = new Domain(url);
            Anchor anchor = new Anchor(domain, url);
            WebPage webPage = new WebPage(anchor);
            webPage.getDocumentFromWeb();

            PaginatedPageCaseSet paginatedPageCaseSet = new PaginatedPageCaseSet(webPage);

            paginatedPageCaseSets.add(paginatedPageCaseSet);

        }

    }

    public void createPageList(WebPage webPage) {
        String pageSelect = webPage.getDocument().getElementsByClass("pagecount").toString();

        Document document = Jsoup.parse(pageSelect);
        Elements options = document.select("strong");

        for (Element element : options.subList(1, options.size())) {

            pageCount = Integer.parseInt(element.text());

        }
        for (int i = 1; i < pageCount + 1; i++) {

            String link = webPage.getAnchor().getAnchorUrl();
            link += "&" + "pgnum=" + i;
            pagesList.add(link);

        }
    }

    public List<String> getPages() {
        return pagesList;
    }

    public List getPaginatedPageCaseSet() {
        return paginatedPageCaseSets;
    }
}
