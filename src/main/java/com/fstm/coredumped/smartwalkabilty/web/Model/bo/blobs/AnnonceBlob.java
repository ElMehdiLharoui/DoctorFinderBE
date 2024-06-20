package com.fstm.coredumped.smartwalkabilty.web.Model.bo.blobs;

import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Annonce;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Image;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Site;
import com.fstm.coredumped.smartwalkabilty.web.Model.dao.DAOCategorie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AnnonceBlob {
    private int id;
    private String  Description, url, Token;
    private int[] sites;
    private String[] url_optionnel;
    private int id_cat;




    public boolean verifyInfos() {
        if ( Description == null || url == null || Token == null|| id_cat==0 || sites == null )
            return false;
        return sites.length != 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public int[] getSites() {
        return sites;
    }

    public void setSites(int[] sites) {
        this.sites = sites;
    }

    public String[] getUrl_optionnel() {
        return url_optionnel;
    }

    public void setUrl_optionnel(String[] url_optionnel) {
        this.url_optionnel = url_optionnel;
    }

    public int getId_cat() {
        return id_cat;
    }

    public void setId_cat(int id_cat) {
        this.id_cat = id_cat;
    }

    public void FillAnnonceFromBlob(Annonce annonce) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        annonce.setDescription(Description);
        annonce.setUrlPrincipalImage(url);
        annonce.setCategorie(DAOCategorie.getDaoCategorie().getById(id_cat));

        for (int st : sites) {
            Site site = new Site();
            site.setId(st);
            annonce.AddSite(site);
        }
        String[] urlop = url_optionnel;
        if (urlop != null)
            for (String url : urlop) {
                annonce.AddImage(new Image(url, annonce));
            }
    }

}
