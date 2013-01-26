package org.crawler;

import java.security.GeneralSecurityException;

/**
 * Created by IntelliJ IDEA.
 * User: liorsolomon
 * Date: 11/8/12
 * Time: 2:00 AM
 * Market Crawler Interface
 */
public interface MarketCrawler{

    void init(String url) throws GeneralSecurityException;
    int getReviewCount();
    String getReviewerName(int reviewNumber);
    String getReviewerMobileType(int reviewNumber);
    String getReviewerMobileVersion(int reviewNumber);
    String getReviewTitle(int reviewNumber);
    String getReviewComment(int reviewNumber);
    String getReviewRank(int reviewNumber);
    String getReviewDate(int reviewNumber);
    
}
