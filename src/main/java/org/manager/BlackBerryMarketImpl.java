package org.manager;

import org.crawler.BlackBerryCrawlerImpl;
import org.dao.SessionFactoryHelper;
import org.objects.CommentBO;
import org.objects.MarketBO;

import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liorsolomon
 * Date: 11/18/12
 * Time: 3:18 PM
 * Apple market BL manager
 */
public class BlackBerryMarketImpl implements Market {
    private static final int BLACKBERRY_MARKET_ID = 3;
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
        BlackBerryCrawlerImpl blackBerryCrawler = new BlackBerryCrawlerImpl();
        try {
            blackBerryCrawler.init(url);

            SessionFactoryHelper dao = new SessionFactoryHelper();
            try{
                dao.beginTransaction();

                MarketBO marketBO = dao.findById(MarketBO.class, BLACKBERRY_MARKET_ID);

                for(int i = 0;i < blackBerryCrawler.getReviewCount();i++){
                    CommentBO commentBO = new CommentBO(blackBerryCrawler.getReviewerName(i),
                                                        blackBerryCrawler.getReviewDate(i),
                                                        null,
                                                        null,
                                                        blackBerryCrawler.getReviewTitle(i),
                                                        blackBerryCrawler.getReviewComment(i),
                                                        blackBerryCrawler.getReviewRank(i),
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
