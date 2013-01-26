package org.manager;

import org.dao.SessionFactoryHelper;
import org.objects.MarketBO;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: liorsolomon
 * Date: 11/8/12
 * Time: 2:23 AM
 * The following class determines which crawler market should be initiated in order to
 * scrap the comments per app on the determined market.
 */
public class MarketFactory {
    private static final String MY_MARKETS_CONFIGURATION = "markets";
    private static final String MARKET_IMPL_CLASS_NAME = "MarketImpl";
    private static List<String> marketsMapping = new ArrayList<String>();

    public void getMarkets() {
        SessionFactoryHelper sessionFactoryHelper = new SessionFactoryHelper();

        List<MarketBO> marketBOList = sessionFactoryHelper.findAll(MarketBO.class);
        for(MarketBO marketBO : marketBOList){
            String className = marketBO.getMarketName();
            Package pack = this.getClass().getPackage();
            String packagePath = pack.getName();
            try {
                if(className!=null) {
                    Class cls = Class.forName(packagePath +
                            "." + className + MARKET_IMPL_CLASS_NAME);
                    Market market = (Market) cls.newInstance();
                    market.setName(className);
                    market.setUrl(marketBO.getUrl());
                    market.getComments();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
