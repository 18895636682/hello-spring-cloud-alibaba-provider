package com.zhuzhi.spring.cloud.alibaba.provider.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "mobileloc")
@Getter
@Setter
public class Mobileloc implements Serializable {

    private static final long serialVersionUID = -7340608884964030405L;

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "`prefix`")
    private String prefix;

    @Column(name = "mobilenumber")
    private String mobilenumber;

    @Column(name = "mobileareaprovince")
    private String mobileareaprovince;

    @Column(name = "mobileareacity")
    private String mobileareacity;

    @Column(name = "mobiletype")
    private String mobiletype;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "areacode")
    private String areacode;

    @Column(name = "citycode")
    private String citycode;

    public Mobileloc() {
    }

    public Mobileloc(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return mobilenumber
     */
    public String getMobilenumber() {
        return mobilenumber;
    }

    /**
     * @param mobilenumber
     */
    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    /**
     * @return mobileareaprovince
     */
    public String getMobileareaprovince() {
        return mobileareaprovince;
    }

    /**
     * @param mobileareaprovince
     */
    public void setMobileareaprovince(String mobileareaprovince) {
        this.mobileareaprovince = mobileareaprovince;
    }

    /**
     * @return mobileareacity
     */
    public String getMobileareacity() {
        return mobileareacity;
    }

    /**
     * @param mobileareacity
     */
    public void setMobileareacity(String mobileareacity) {
        this.mobileareacity = mobileareacity;
    }

    /**
     * @return mobiletype
     */
    public String getMobiletype() {
        return mobiletype;
    }

    /**
     * @param mobiletype
     */
    public void setMobiletype(String mobiletype) {
        this.mobiletype = mobiletype;
    }

    /**
     * @return postcode
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * @param postcode
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * @return areacode
     */
    public String getAreacode() {
        return areacode;
    }

    /**
     * @param areacode
     */
    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    /**
     * @return citycode
     */
    public String getCitycode() {
        return citycode;
    }

    /**
     * @param citycode
     */
    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", prefix=").append(prefix);
        sb.append(", mobilenumber=").append(mobilenumber);
        sb.append(", mobileareaprovince=").append(mobileareaprovince);
        sb.append(", mobileareacity=").append(mobileareacity);
        sb.append(", mobiletype=").append(mobiletype);
        sb.append(", postcode=").append(postcode);
        sb.append(", areacode=").append(areacode);
        sb.append(", citycode=").append(citycode);
        sb.append("]");
        return sb.toString();
    }
}