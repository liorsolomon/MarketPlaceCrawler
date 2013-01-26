import org.manager.MarketFactory;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: liorsolomon
 * Date: 11/18/12
 * Time: 11:35 AM
 * Market Crawler main
 */
public class MarketCrawler {
    //TODO: Chage this to run as a service
    public static void main(String[] args) {
        MarketFactory marketFactory = new MarketFactory();
        marketFactory.getMarkets();
    }

    /**
     * Created with IntelliJ IDEA.
     * User: liorsolomon
     * Date: 11/18/12
     * Time: 11:13 PM
     * To change this template use File | Settings | File Templates.
     */
    public static class SandBox {
        static Properties prop = System.getProperties();
        public static void main(String[] args) {

            System.getProperties();
            prop.setProperty("java.class.path", getClassPath());
            System.out.println("java.class.path now = " + getClassPath());
        }

        static String getClassPath() {
            return prop.getProperty("java.class.path", null);
        }
    }
}
