package org.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import org.jaxen.JaxenException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: lior
 * Date: 1/26/13
 * Time: 2:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class BlackBerryCrawlerImpl implements MarketCrawler {
    HtmlPage page = null;
    List<HtmlDivision> reviewDivs = null;
    static final String CONST_BLACKBERRY_REVIEWS_DIV = "reviewTileList_box_tileList";


    @Override
    public void init(String url) throws GeneralSecurityException {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_2);
        webClient.setUseInsecureSSL(true);
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setJavaScriptEnabled(true);

        try {
            page = (HtmlPage) webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create array of review html elements
        reviewDivs = new ArrayList<HtmlDivision>();
        try {
            HtmlDivision division = (HtmlDivision) page.getHtmlElementById(CONST_BLACKBERRY_REVIEWS_DIV);
            reviewDivs = (List<HtmlDivision>) division.getByXPath("//div[@class='awwsReview']");
        } catch (JaxenException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getReviewCount() {
        return reviewDivs.size();
    }

    @Override
    public String getReviewerName(int reviewNumber) {
        String reviwerName = null;
        List<HtmlDivision> divs;
        try {
            divs = (List<HtmlDivision>) reviewDivs.get(reviewNumber).getByXPath("//div[@class='awwsReviewUserName awwsReviewHeader']");
            for(HtmlDivision div : divs){
                reviwerName = div.asText();
                System.out.println("Name: " + div.asText());
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return reviwerName;
    }

    @Override
    public String getReviewerMobileType(int reviewNumber) {
        return null;  //Not supported by BlackBerry
    }

    @Override
    public String getReviewerMobileVersion(int reviewNumber) {
        return null;  //Not supported by Blackberry
    }

    @Override
    public String getReviewTitle(int reviewNumber) {
        String reviewTitle = null;
        List<HtmlDivision> divs;
        try {
            divs = (List<HtmlDivision>) reviewDivs.get(reviewNumber).getByXPath("//span[@class='awwsReviewTitle']");
            for(HtmlDivision div : divs){
                reviewTitle = div.asText();
                System.out.println("Title: " + div.asText());
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return reviewTitle;
    }

    @Override
    public String getReviewComment(int reviewNumber) {
        String reviewComment = null;
        List<HtmlDivision> divs;
        try {
            divs = (List<HtmlDivision>) reviewDivs.get(reviewNumber).getByXPath("//div[@class='awwsReviewBody']");
            for(HtmlDivision div : divs){
                reviewComment = div.asText();
                System.out.println("Comment: " + div.asText());
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return reviewComment;
    }

    @Override
    public String getReviewRank(int reviewNumber) {
        String reviewRank = null;
        List<HtmlDivision> divisions;
        try {
            divisions = (List<HtmlDivision>) reviewDivs.get(reviewNumber).getByXPath("//div[@class='awwsReviewRating']");
            for(HtmlDivision div : divisions){
                reviewRank = convertRankStringToNumber(div.getAttribute("src"));
                System.out.println("rank: " + reviewRank);
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return reviewRank;
    }

    @Override
    public String getReviewDate(int reviewNumber) {
        String reviewDate = null;
        List<HtmlDivision> divisions;
        try {
            divisions = (List<HtmlDivision>) reviewDivs.get(reviewNumber).getByXPath("//div[@class='awwsReviewDate awwsReviewHeader']");
            for(HtmlDivision div : divisions){
                reviewDate = div.asText();
                System.out.println("date: " + reviewDate);
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return reviewDate;
    }

    private String convertRankStringToNumber(String rankString){
        String rankNumber = null;
        Pattern versionPattern = Pattern.compile("[0-9][0-9]");
        Matcher matcher = versionPattern.matcher(rankString);
        while (matcher.find()) {
            rankNumber = matcher.group(0);
        }
        Integer rank = Integer.parseInt(rankNumber)/2;
        return rank.toString();
    }

    private String cleanDate(String date){
        return date.replace("-","");
    }
}
