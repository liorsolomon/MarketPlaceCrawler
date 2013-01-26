package org.manager;

import org.crawler.AppleCrawlerImpl;
import org.dao.SessionFactoryHelper;
import org.objects.CommentBO;
import org.objects.MarketBO;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liorsolomon
 * Date: 11/18/12
 * Time: 3:18 PM
 * Apple market BL manager
 */
public class AppleMarketImpl implements Market {
    private static final int APPLE_MARKET_ID = 2;
    String name = null;
    String url = null;
    List<String> names = null;
    List<String> comments = null;
    List<String> titles = null;
    List<String> ranks = null;

    @Override
    public void setName(String name) {
        name = name;
    }

    @Override
    public void setUrl(String URL) {
        url = URL;
    }

    @Override
    public void getComments() {
        AppleCrawlerImpl appleCrawler = new AppleCrawlerImpl();
        try {
            appleCrawler.init(url);

            SessionFactoryHelper dao = new SessionFactoryHelper();
            try{
                dao.beginTransaction();

                MarketBO marketBO = dao.findById(MarketBO.class, APPLE_MARKET_ID);

                for(int i = 0;i < appleCrawler.getReviewCount();i++){
                    CommentBO commentBO = new CommentBO(appleCrawler.getReviewerName(i),
                                                        null,
                                                        null,
                                                        null,
                                                        appleCrawler.getReviewTitle(i),
                                                        appleCrawler.getReviewComment(i),
                                                        appleCrawler.getReviewRank(i),
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
