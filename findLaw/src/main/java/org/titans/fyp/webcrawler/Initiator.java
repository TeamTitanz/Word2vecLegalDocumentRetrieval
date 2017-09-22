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
import org.titans.fyp.webcrawler.findLaw.PaginatedPageCaseSet;
import org.titans.fyp.webcrawler.findLaw.Pagination;
import org.titans.fyp.webcrawler.findLaw.QueryBuilder;
import org.titans.fyp.webcrawler.models.Anchor;
import org.titans.fyp.webcrawler.models.Domain;
import org.titans.fyp.webcrawler.models.WebPage;
import org.titans.fyp.webcrawler.utils.FindLawConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The initiate the spider function.
 */
public class Initiator {

    final static Logger logger = Logger.getLogger(Initiator.class);
    public static int caseID = 0;

    public static void main(String[] args) {

        logger.info("Initializing the spider function...");

        String url = "http://caselaw.findlaw.com";

        try {
            FindLawConfiguration findLawConfig = new FindLawConfiguration(args);

            Domain domain = new Domain(url);
            Anchor anchor = new Anchor(domain, url);
            WebPage webPage = new WebPage(anchor);
            webPage.getDocumentFromWeb();
            PageCollector.setDomain(url);

//            PageCollector.Crawl("http://caselaw.findlaw.com/summary/opinion/us-supreme-court/2016/06/20/276801.html",
//                    "http://caselaw.findlaw.com/us-supreme-court/15-446.html");

            logger.info("Building the URL list\nIt will take several minutes. Please wait...");
            //by this, it will print the Case URl, and the detiled case URL
            QueryBuilder queryBuilder = new QueryBuilder(webPage, findLawConfig.getTopicArray());
            ArrayList<Pagination> paginatedURLs = (ArrayList<Pagination>) queryBuilder.getPaginatedURLs();

            logger.info("Crawling the web pages\\nIt will take several minutes. Please wait...");
            for (Pagination pagination : paginatedURLs) {
                ArrayList<PaginatedPageCaseSet> paginatedPageCaseSets =
                        (ArrayList<PaginatedPageCaseSet>) pagination.getPaginatedPageCaseSet();

                for (PaginatedPageCaseSet pageCaseSet : paginatedPageCaseSets) {

                    HashMap<String, String> caseMap = (HashMap<String, String>) pageCaseSet.getCaseMap();

                    for (Map.Entry<String, String> entry : caseMap.entrySet()) {
                        PageCollector.Crawl(entry.getKey(), entry.getValue());
                    }

                }
            }
            logger.info("crawling completed.");

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }
}
