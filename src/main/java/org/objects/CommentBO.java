package org.objects;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: liorsolomon
 * Date: 11/8/12
 * Time: 4:23 PM
 * User comments data entity
 */
@Entity()
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "T_RP_MONITORED_COMMENTS")
public class CommentBO {
    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "seqhilo",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence", value = "T_RP_MONITORED_COMMENTS_SEQ")
            }
    )
    @Column(name = "ID")
    private int id;

    @Column(name = "REVIEWER_NAME")
    private String reviewerName;

    @Column(name = "REVIEW_DATE")
    private Date reviewDate;

    @Column(name = "REVIEWER_MOBILE_TYPE")
    private String reviewerMobileType;

    @Column(name = "REVIEWER_MOBILE_VERSION")
    private String reviewerMobileVersion;

    @Column(name = "TITLE")
    private String commentTitle;

    @Column(name = "TEXT")
    private String commentText;

    @Column(name = "RATE")
    private String rate;

    @ManyToOne
    @JoinColumn(name="MARKET_ID")
    private MarketBO marketBO;

    public CommentBO(){

    }

    public CommentBO(String reviewerName, String reviewDate, String reviewerMobileType, String reviewerMobileVersion,
                     String commentTitle, String commentText, String rate, MarketBO marketBO) throws Exception {
        this.setReviewerName(reviewerName == null ? "" : reviewerName);
        this.setReviewDate(reviewDate == null ? "" : reviewDate);
        this.setReviewerMobileType(reviewerMobileType == null ? "" : reviewerMobileType);
        this.setReviewerMobileVersion(reviewerMobileVersion == null ? "" : reviewerMobileVersion);
        this.setCommentTitle(commentTitle == null ? "" : commentTitle);
        this.setCommentText(commentText == null ? "" : commentText);
        this.setRate(rate == null ? "" : rate);
        if (marketBO == null){
            throw new Exception("All comments have to get a market id");
        } else {
            this.setMarketBO(marketBO);
        }
    }


    private void setId(int id) {
        this.id = id;
    }

    private void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    private void setReviewerMobileType(String reviewerMobileType) {
        this.reviewerMobileType = reviewerMobileType;
    }

    private void setReviewerMobileVersion(String reviewerMobileVersion) {
        this.reviewerMobileVersion = reviewerMobileVersion;
    }

    private void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    private void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    private void setRate(String rate) {
        this.rate = rate;
    }

    private Date getReviewDate() {
        return reviewDate;
    }

    private void setReviewDate(String reviewDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy");
        try {
            this.reviewDate = new java.sql.Date(sdf.parse(reviewDate).getTime());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    private MarketBO getMarketBO() {
        return marketBO;
    }

    private void setMarketBO(MarketBO marketBO) {
        this.marketBO = marketBO;
    }
}


