package org.titans.fyp.webcrawler.findLaw;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.titans.fyp.webcrawler.database.DBConnection;
import org.titans.fyp.webcrawler.models.Anchor;
import org.titans.fyp.webcrawler.models.Domain;
import org.titans.fyp.webcrawler.models.WebPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Buddhi on 9/23/2017.
 */
public class UrlCollector {

    final static Logger logger = Logger.getLogger(UrlCollector.class);
    private String courtName;
    private List<String> pagesList = new ArrayList();
    private List<String> summaryPageUrlList = new ArrayList();

    public UrlCollector(String courtName) {

        this.courtName = "http://caselaw.findlaw.com/search?search_type=party&court="
                + courtName + "&date_start=17600101&date_end=20170923";
        setPagesList();

        DBConnection.setDbName("_" + courtName);
        logger.info("Court: " + courtName);
        logger.info("Database Name: oblie_" + courtName);
        DBConnection.setDbUserName("root");
        logger.info("Database UserName: " + "root");
        DBConnection.setDbPassword("root");

    }

    public void setPagesList() {
        try {
            Domain domain = new Domain(courtName);
            Anchor anchor = new Anchor(domain, courtName);
            WebPage webPage = new WebPage(anchor);
            webPage.getDocumentFromWeb();

            String pageSelect = webPage.getDocument().getElementsByClass("pagecount").toString();
            Document document = Jsoup.parse(pageSelect);
            Elements options = document.select("strong");
            int pageCount = Integer.parseInt(options.subList(1, options.size()).get(0).text());
//            System.out.println(pageCount);
            String baseLink = webPage.getAnchor().getAnchorUrl();
            for (int i = 1; i <= pageCount; i++) {
                pagesList.add(baseLink + "&" + "page=" + i);
                break;//*****************************************
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public List<String> getSummaryPageUrlList() throws Exception {

        for (String url : pagesList) {
            Domain domain = new Domain(url);
            Anchor anchor = new Anchor(domain, url);
            WebPage webPage = new WebPage(anchor);
            webPage.getDocumentFromWeb();

            String caseLawTable = webPage.getDocument().getElementById("srpcaselaw").toString();
            Document document = Jsoup.parse(caseLawTable);
            Elements options = document.select("a[href]");
            for (Element element : options) {
                System.out.println(element.attr("href")+"\t"+ element.attr("title"));

//                summaryPageUrlList.add(element.attr("href"));
            }
            break;//*****************************************
        }
        return summaryPageUrlList;
    }

}
