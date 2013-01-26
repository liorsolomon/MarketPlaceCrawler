package org.objects;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: liorsolomon
 * Date: 11/8/12
 * Time: 3:37 PM
 * The Market place entity
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "T_RP_MONITORED_MARKETS")
public class MarketBO implements Serializable {
    @Id
    @Column(name = "MARKET_ID")
    private int id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "URL")
    private String url;

    @OneToMany(mappedBy="marketBO")
    private Set<CommentBO> comments;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinTable(name="T_RP_MONITORED_APPLICATIONS",
            joinColumns = @JoinColumn(name="ID"),
            inverseJoinColumns = @JoinColumn(name="APPLICATION_ID")
    )

    public String getMarketName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Set<CommentBO> getCommentBOSet() {
        return comments;
    }

    public void setCommentBOSet(Set<CommentBO> comments) {
        this.comments = comments;
    }

    public void setId(int id) {
        this.id = id;
    }
}
