package org.manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liorsolomon
 * Date: 11/18/12
 * Time: 3:19 PM
 * Market BL manager interface
 */
public interface Market {
    String name = null;
    List<String> comments = null;

    void setName(String name);
    void setUrl(String URL);
    void getComments();
}
