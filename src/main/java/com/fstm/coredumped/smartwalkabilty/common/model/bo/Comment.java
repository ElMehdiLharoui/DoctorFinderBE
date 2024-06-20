package com.fstm.coredumped.smartwalkabilty.common.model.bo;

import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Site;

import java.util.Date;

public class Comment {
    private int id;
    private double rating;
    private String comment;
    private int idSite;
    private String idUser;
    private Site site;
    private Date createdAt = new Date();

    public Comment() {
    }

    public Comment(double rating, String comment, int idSite, String idUser) {
        this.rating = rating;
        this.comment = comment;
        this.idSite = idSite;
        this.idUser = idUser;
    }

    public Comment(int id, double rating, String comment, int idSite, String idUser, Site site) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.idSite = idSite;
        this.idUser = idUser;
        this.site = site;
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
}
