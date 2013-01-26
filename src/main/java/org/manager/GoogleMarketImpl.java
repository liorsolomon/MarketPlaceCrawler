package org.manager;

import org.crawler.GoogleCrawlerImpl;
import org.dao.SessionFactoryHelper;
import org.objects.CommentBO;
import org.objects.MarketBO;

import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liorsolomon
 * Date: 11/18/12
 * Time: 11:23 PM
 * Google play market BL manager
 */
public class GoogleMarketImpl implements Market {
    private static final int GOOGLE_MARKET_ID = 1;
    String name = null;
    String url = null;

    @Override
    public void setName(String name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setUrl(String URL) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void getComments() {
        GoogleCrawlerImpl googleCrawler = new GoogleCrawlerImpl();
        try {
            googleCrawler.init(url);

            SessionFactoryHelper dao = new SessionFactoryHelper();

            try{
                dao.beginTransaction();

                MarketBO marketBO = dao.findById(MarketBO.class, GOOGLE_MARKET_ID);

                for(int i = 0;i < googleCrawler.getReviewCount();i++){
                    CommentBO commentBO = new CommentBO(googleCrawler.getReviewerName(i),
                                                        googleCrawler.getReviewDate(i),
                                                        googleCrawler.getReviewerMobileType(i),
                                                        googleCrawler.getReviewerMobileVersion(i),
                                                        googleCrawler.getReviewTitle(i),
                                                        googleCrawler.getReviewComment(i),
                                                        googleCrawler.getReviewRank(i),
                                                        marketBO);
                    dao.save(commentBO);
                }
                dao.commit();
            } catch(Exception ex){
                System.out.println(ex.getMessage());
                dao.rollBack();
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

    }
}
