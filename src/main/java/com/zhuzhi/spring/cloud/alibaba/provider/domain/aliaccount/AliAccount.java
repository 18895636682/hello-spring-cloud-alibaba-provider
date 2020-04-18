package com.zhuzhi.spring.cloud.alibaba.provider.domain.aliaccount;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Table(name = "aliAccountManagement")
public class AliAccount implements Serializable {
    private static final long serialVersionUID = -234347111455199650L;

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "`user`")
    private String user;

    @Column(name = "appkey")
    private String appkey;

    @Column(name = "AccessKeyID")
    private String accesskeyid;

    @Column(name = "AccessKeySecret")
    private String accesskeysecret;

    @Column(name = "addTime")
    private Date addtime;

    @Column(name = "editTime")
    private Date edittime;

    public AliAccount() {
    }

    public AliAccount(String appkey) {
        this.appkey = appkey;
    }
}