package com.fstm.coredumped.smartwalkabilty.common.model.bo;

import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Site;

import java.util.Date;

public class Comment {
    private int id;
    private double rating;
    private String comment;
    private int idSite;
    private String idUser;
    private String displayName;
    private Site site;
    private Date createdAt = new Date();

    public Comment() {
    }

    public Comment(double rating, String comment, int idSite, String idUser, String displayName) {
        this.rating = rating;
        this.comment = comment;
        this.idSite = idSite;
        this.idUser = idUser;
        this.displayName = displayName;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getIdSite() {
        return idSite;
    }

    public void setIdSite(int idSite) {
        this.idSite = idSite;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
