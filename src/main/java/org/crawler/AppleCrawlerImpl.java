package org.crawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jaxen.JaxenException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: liorsolomon
 * Date: 11/25/12
 * Time: 12:55 AM
 * Apple Appstore scrapper class
 */
public class AppleCrawlerImpl implements MarketCrawler{
    HtmlPage page = null;
    List<HtmlDivision> reviewDivs = null;

    @Override
    public void init(String url) throws GeneralSecurityException {
        WebClient webClient = new WebClient();
        webClient.setUseInsecureSSL(true);
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setJavaScriptEnabled(false);
        try {
            page = (HtmlPage) webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create array of review html elements
        reviewDivs = new ArrayList<HtmlDivision>();
        try {
            reviewDivs = (List<HtmlDivision>) page.getByXPath("//div[@class='customer-review']");
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
        List<HtmlSpan> spans;
        try {
            spans = (List<HtmlSpan>) reviewDivs.get(reviewNumber).getByXPath("//span[@class='user-info']");
            for(HtmlSpan span : spans){
                reviwerName = span.asText();
                System.out.println("Name: " + span.asText());
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return reviwerName;
    }

    @Override
    public String getReviewerMobileType(int reviewNumber) {
        //unsupported by the apple appstore
        return null;
    }

    @Override
    public String getReviewerMobileVersion(int reviewNumber) {
        //unsupported by the apple appstore
        return null;
    }

    @Override
    public String getReviewTitle(int reviewNumber) {
        String reviewTitle = null;
        List<HtmlSpan> spans;
        try {
            spans = (List<HtmlSpan>) reviewDivs.get(reviewNumber).getByXPath("//span[@class='customerReviewTitle']");
            for(HtmlSpan span : spans){
                reviewTitle = span.asText();
                System.out.println("Title: " + span.asText());
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return reviewTitle;
    }

    @Override
    public String getReviewComment(int reviewNumber) {
        String reviewComment = null;
        List<HtmlParagraph> paragraphs;
        try {
            paragraphs = (List<HtmlParagraph>) reviewDivs.get(reviewNumber).getByXPath("//p[@class='content']");
            for(HtmlParagraph paragraph : paragraphs){
                reviewComment = paragraph.asText();
                System.out.println("Comment: " + paragraph.asText());
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
            divisions = (List<HtmlDivision>) reviewDivs.get(reviewNumber).getByXPath("//div[@class='rating']");
            for(HtmlDivision div : divisions){
                reviewRank = convertRankStringToNumber(div.getAttribute("aria-label"));
                System.out.println("rank: " + convertRankStringToNumber(div.getAttribute("aria-label")));
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return reviewRank;
    }

    @Override
    public String getReviewDate(int reviewNumber) {
        //unsupported by the apple appstore
        return null;
    }

    private String convertRankStringToNumber(String rankString){
        String rankNumber = null;
        Pattern versionPattern = Pattern.compile("[0-9]");
        Matcher matcher = versionPattern.matcher(rankString);
        while (matcher.find()) {
            rankNumber = matcher.group(0);
        }
        return rankNumber;
    }
}
