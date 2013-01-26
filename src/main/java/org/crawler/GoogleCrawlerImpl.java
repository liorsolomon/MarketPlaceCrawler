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
 * Date: 11/19/12
 * Time: 12:14 AM
 * Google Play market scrapper class
 */
public class GoogleCrawlerImpl implements MarketCrawler {
    HtmlPage page = null;
    List<HtmlDivision> reviewDivs = null;
    static final String CONST_START_MOBILE_TYPE_STRING_PATTERN = " - ";
    static final String CONST_END_MOBILE_TYPE_STRING_PATTERN = "with";
    static final String CONST_START_MOBILE_VERSION_STRING_PATTERN = "version ";
    static final String CONST_GOOGLE_PLAY_OVERVIEW_TAB = "details-tab-1";
    static final String CONST_GOOGLE_PLAY_REVIEWS_TAB = "details-tab-2";
    static final String CONST_GOOGLE_PLAY_REVIEWS_DIV = "doc-user-reviews-page num-pagination-page";

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
            HtmlDivision division = (HtmlDivision) page.getHtmlElementById(CONST_GOOGLE_PLAY_REVIEWS_TAB);
            reviewDivs = (List<HtmlDivision>) division.getByXPath("//div[@class='doc-review']");
        } catch (JaxenException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getReviewCount(){
        return reviewDivs.size();
    }

    @Override
    public String getReviewerName(int reviewNumber) {
        String reviewerName = null;
        try {
            HtmlDivision division = (HtmlDivision) reviewDivs.get(reviewNumber);
            List<HtmlSpan> spans = (List<HtmlSpan>) division.getByXPath("//span[@class='doc-review-author  g-hovercard']");

            for(HtmlSpan span : spans){
                reviewerName = span.asText();
                System.out.println("Name: " + span.asText());
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return reviewerName;
    }

    @Override
    public String getReviewerMobileType(int reviewNumber) {

        String reviewMobileType = null;

        HtmlDivision division = (HtmlDivision) reviewDivs.get(reviewNumber);
        reviewMobileType = cleanGoogleMobileType(division.asText());
        System.out.println("mobile type: " + cleanGoogleMobileType(division.asText()));

        return reviewMobileType;
    }

    @Override
    public String getReviewerMobileVersion(int reviewNumber) {

        String reviewerMobileVersion = null;


        HtmlDivision division = (HtmlDivision) reviewDivs.get(reviewNumber);
        reviewerMobileVersion = cleanGoogleMobileAppVersion(division.asText());
        System.out.println("mobile version: " + cleanGoogleMobileAppVersion(division.asText()));

        return reviewerMobileVersion;
    }

    @Override
    public String getReviewTitle(int reviewNumber) {

        String reviewTitle = null;

        try {
            HtmlDivision division = (HtmlDivision) reviewDivs.get(reviewNumber);
            List<HtmlHeader4> header4s = (List<HtmlHeader4>) division.getByXPath("//h4[@class='review-title']");
            for(HtmlHeader4 header4 : header4s){
                reviewTitle = header4.asText();
                System.out.println("Title: " + header4.asText());
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return reviewTitle;
    }

    @Override
    public String getReviewComment(int reviewNumber) {
        String reviewComment = null;
        List<HtmlParagraph> paragraphs = new ArrayList<HtmlParagraph>();
        try {
            HtmlDivision division = (HtmlDivision) reviewDivs.get(reviewNumber);
            paragraphs = (List<HtmlParagraph>) division.getByXPath("//p[@class='review-text']");
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
        List<HtmlDivision> divisions = new ArrayList<HtmlDivision>();
        try {
            HtmlDivision division = (HtmlDivision) reviewDivs.get(reviewNumber);
            divisions = (List<HtmlDivision>) division.getByXPath("//div[@class='ratings goog-inline-block']");
            for(HtmlDivision div : divisions){
                reviewRank = convertRankStringToNumber(div.getAttribute("title"));
                System.out.println("rank: " + convertRankStringToNumber(div.getAttribute("title")));
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return reviewRank;
    }

    @Override
    public String getReviewDate(int reviewNumber) {

        String reviewDate = null;
        List<HtmlSpan> spans = new ArrayList<HtmlSpan>();
        try {
            HtmlDivision division = (HtmlDivision) reviewDivs.get(reviewNumber);
            spans = (List<HtmlSpan>) division.getByXPath("//span[@class='doc-review-date']");
            for(HtmlSpan span : spans){
                reviewDate = cleanGoogleDate(span.asText());
                System.out.println("date: " + cleanGoogleDate(span.asText()));
            }
        } catch (JaxenException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return reviewDate;
    }

    private String cleanGoogleMobileType(String mobileType) {
        String newMobileType = null;

        int startStringIndex = mobileType.lastIndexOf(CONST_START_MOBILE_TYPE_STRING_PATTERN) + CONST_START_MOBILE_TYPE_STRING_PATTERN.length();
        int versionStringIndex = mobileType.toLowerCase().indexOf(CONST_END_MOBILE_TYPE_STRING_PATTERN);

        if (versionStringIndex < 0) {
            versionStringIndex = mobileType.toLowerCase().indexOf(CONST_START_MOBILE_VERSION_STRING_PATTERN);
        }

        newMobileType = mobileType.substring(startStringIndex, versionStringIndex);
        return newMobileType;
    }

    private String cleanGoogleMobileAppVersion(String mobileVersion) {
        String version = null;
        Pattern versionPattern = Pattern.compile("[0-9].[0-9].[0-9]");
        Matcher matcher = versionPattern.matcher(mobileVersion);
        while (matcher.find()) {
            version = matcher.group(0);
        }
        return version;
    }

    private String cleanGoogleDate(String date){
        return date.replace(" - ","");
    }

    private String convertRankStringToNumber(String rankString){
        String rankNumber = null;
        Pattern versionPattern = Pattern.compile("[0-9].[0-9]");
        Matcher matcher = versionPattern.matcher(rankString);
        while (matcher.find()) {
            rankNumber = matcher.group(0);
        }
        return rankNumber;
    }
}
