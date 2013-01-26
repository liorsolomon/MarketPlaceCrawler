package org.objects;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: liorsolomon
 * Date: 11/8/12
 * Time: 3:57 PM
 * The scanned application data entity
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "T_RP_MONITORED_APPLICATIONS")
public class AppBO {
    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
    private String name;
}
