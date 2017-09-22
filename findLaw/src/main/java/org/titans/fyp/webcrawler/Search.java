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

import java.util.Arrays;
import java.util.List;

/**
 * Created by Buddhi on 6/11/2017.
 */
public class Search {

    List<String> oldCharList = Arrays.asList(" ", "'", ",", "$", "&", ":", "/", "?");
    List<String> newCharList = Arrays.asList("+", "%27", "%2C", "%24", "%26", "%3A", "%2F", "%3F");


    public String createURL(String name) {
        name = replaceChar(name);
        return "http://lawcrawler.findlaw.com/LCsearch.html?restrict=lp&client=lp&entry=" + name;
    }

    private String replaceChar(String name) {
        String newName = "";
        for (int x = 0; x < name.length(); x++) {
            String cha = String.valueOf(name.charAt(x));
            if (oldCharList.contains(cha)) {
                newName += newCharList.get(oldCharList.indexOf(cha));
            } else {
                newName += cha;
            }
        }
        return newName;
    }

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
        Search se = new Search();
        se.getReadPage(se.createURL("clark v. us dep't of agric."));
    }


}
