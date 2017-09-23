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
import org.titans.fyp.webcrawler.database.DBConnection;
import org.titans.fyp.webcrawler.findLaw.UrlCollector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * The initiate the spider function.
 */
public class Initiator {

    final static Logger logger = Logger.getLogger(Initiator.class);
    public static int caseID = 589;

    public static void main(String[] args) {

        logger.info("Initializing the spider function...");
        String url = "http://caselaw.findlaw.com";

        try {
            PageCollector.setDomain(url);

            String courtName = "us-supreme-court";
            DBConnection.setDbName("_" + courtName);
            logger.info("Court: " + courtName);
            logger.info("Database Name: oblie_" + courtName);
            DBConnection.setDbUserName("root");
            logger.info("Database UserName: " + "root");
            DBConnection.setDbPassword("root");

            logger.info("Reading the URL list from \"mentionUnique.txt\"");
            List<String> summaryPageUrlList = new ArrayList();
            BufferedReader br = new BufferedReader(new FileReader("mentionUnique.txt"));
            try {
                String line = br.readLine();
                while (line != null) {
                    summaryPageUrlList.add(line);
                    line = br.readLine();
                }
                logger.info(summaryPageUrlList.size());
            } finally {
                br.close();
            }


            logger.info("Crawling has start. Please wait...");
            for (String summaryPage : summaryPageUrlList) {
                PageCollector.Crawl(summaryPage);
            }
            logger.info("crawling completed.");


        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }
}
