package com.fstm.coredumped.smartwalkabilty.web.Model.dao;

import com.fstm.coredumped.smartwalkabilty.common.model.bo.GeoPoint;
import com.fstm.coredumped.smartwalkabilty.core.routing.model.dao.OSMDBConnexion;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Annonce;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Organisation;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Site;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DAOSite implements IDAO<Site>{
    private static DAOSite daoSite=null;

    public DAOSite() {
    }

    public static DAOSite getDaoSite() {
        if(daoSite == null) daoSite= new DAOSite();
        return daoSite;
    }

    @Override
    public boolean Create(Site obj)  {
        boolean returnBool = false;
        try {
            DBConnexion.getCon().setAutoCommit(false);

            PreparedStatement preparedStatement= DBConnexion.getCon().prepareStatement("INSERT INTO site (name ,datecreated,id_organisation,location,address,dureeviste,maxpatient,heuredebut,heurefin,weekday) VALUES (?,?,?,point(?,?),?,?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, obj.getName());
            preparedStatement.setDate(2, new Date(obj.getDateCreated().getTime()));
            preparedStatement.setInt(3,obj.getOrganisation().getId());
            preparedStatement.setDouble(4,obj.getLocalisation().getLaltittude());
            preparedStatement.setDouble(5,obj.getLocalisation().getLongtitude());
            preparedStatement.setString(6,obj.getAdress());
            preparedStatement.setInt(7,obj.getDureeviste());
            preparedStatement.setInt(8,obj.getMaxpatient());
            preparedStatement.setString(9,obj.getHeuredebut());
            preparedStatement.setString(10,obj.getHeurefin());
            preparedStatement.setString(11,obj.getWeekday());
            preparedStatement.executeUpdate();
            ResultSet set= preparedStatement.getGeneratedKeys();

            if(set.next()) obj.setId(set.getInt(1));

            DBConnexion.getCon().commit();
            DBConnexion.getCon().setAutoCommit(true);
            returnBool = true;
        }catch (SQLException e){
            try {
                DBConnexion.getCon().rollback();
            } catch (SQLException ex) {
                System.err.println(ex);
                return  false;

            }
            System.err.println(e);
            return false;
        }
        Connection connection =null;

        try{
            connection = OSMDBConnexion.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO site "
            + "(id, latitude, longitude)"
            + "VALUES (?,?,?)");
            ps.setInt(1,obj.getId());
            ps.setDouble(2,obj.getLocalisation().getLaltittude());
            ps.setDouble(3,obj.getLocalisation().getLongtitude());
            ps.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        }
    }


    public void extractAnnonceSites(Annonce annonce)throws SQLException
    {

        PreparedStatement sql= DBConnexion.getCon().prepareStatement(" SELECT S.* FROM site S join annonces_con_site on id_site=id where id_annonce=?");
        sql.setInt(1,annonce.getId());
        ResultSet set= sql.executeQuery();

        while(set.next())
        {
          Site site =new Site(set.getString("name"),set.getDate("datecreated"));
          site.setId(set.getInt("id"));
          annonce.AddSite(site);
        }

    }
    public Site findById(int id){
        try {
            Organisation organisation =null;
            Site site=null;
            PreparedStatement sql= DBConnexion.getCon().prepareStatement("SELECT *,location[0] as lat,location[1] as lng From site where id=?");
            sql.setInt(1,id);
            ResultSet set= sql.executeQuery();
            if(set.next())
            {
                site=extractSite(set);
                DAOAnnonce.getDAOAnnonce().extractSiteAnnonces(site);
                organisation=DAOOrganisation.getDaoOrganisation().findOrganisationById(set.getInt("id_organisation"));
                site.setOrganisation(organisation);
            }
            return site;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
    }
    public Site findById(int id, List<Integer> cats){
        try {
            Organisation organisation =null;
            Site site=null;
            PreparedStatement sql= DBConnexion.getCon().prepareStatement("SELECT *,location[0] as lat,location[1] as lng From site where id=?");
            sql.setInt(1,id);
            ResultSet set= sql.executeQuery();
            if(set.next())
            {
                site=extractSite(set);
                if(cats.size() == 0)  DAOAnnonce.getDAOAnnonce().extractSiteAnnonces(site);
                else DAOAnnonce.getDAOAnnonce().extractSiteAnnonces_Categorie(site,cats);
                organisation=DAOOrganisation.getDaoOrganisation().findOrganisationById(set.getInt("id_organisation"));
                site.setOrganisation(organisation);
            }
            return site;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
    }

    public Collection<Site> RetreveListSite(int id_organisation){
        try {
            Collection<Site> sites = new LinkedList<Site>();
            Site site=null;
            PreparedStatement sql= DBConnexion.getCon().prepareStatement("SELECT *,location[0] as lat,location[1] as lng From site where id_organisation=?");
            sql.setInt(1,id_organisation);
            ResultSet set= sql.executeQuery();
            while(set.next())
            {
                site=extractSite(set);
                sites.add(site);
            }
            return sites;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
    }
       private Site extractSite(ResultSet set) throws SQLException {
        Site site=new Site();
        site.setId(set.getInt("id"));
        site.setName(set.getString("name"));
        site.setDateCreated(set.getDate("datecreated"));
       // DAOOrganisation.getDaoOrganisation().findOrganisationById(set.getInt("id_organisation"));
           GeoPoint geoPoint = new GeoPoint();
           geoPoint.setLaltittude(set.getDouble("lat"));
           geoPoint.setLongtitude(set.getDouble("lng"));
           site.setLocalisation(geoPoint);
           site.setAdress((set.getString("address")));
           site.setDureeviste(set.getInt("dureeviste"));
           site.setMaxpatient(set.getInt("maxpatient"));
           site.setHeuredebut((set.getString("heuredebut")));
           site.setHeurefin((set.getString("heurefin")));
           site.setWeekday((set.getString("weekday")));
        return site;
    }

    @Override
    public Collection<Site> Retrieve() {

        return null;
    }

    @Override
    public void update(Site obj) {
         try{

             DBConnexion.getCon().setAutoCommit(false);

             PreparedStatement preparedStatement= DBConnexion.getCon().prepareStatement("UPDATE  site SET name =?,datecreated=?,id_organisation=?,location=point(?,?),address=? ,dureeviste=?,maxpatient=?,heuredebut=?,heurefin=?,weekday=? where id=?");
             preparedStatement.setString(1, obj.getName());
             preparedStatement.setDate(2, new Date(obj.getDateCreated().getTime()));
             preparedStatement.setInt(3,obj.getOrganisation().getId());
             preparedStatement.setDouble(4,obj.getLocalisation().getLaltittude());
             preparedStatement.setDouble(5,obj.getLocalisation().getLongtitude());
             preparedStatement.setInt(6,obj.getId());
             preparedStatement.setString(7,obj.getAdress());
             preparedStatement.setInt(8,obj.getDureeviste());
             preparedStatement.setInt(9,obj.getMaxpatient());
             preparedStatement.setString(10,obj.getHeuredebut());
             preparedStatement.setString(11,obj.getHeurefin());
             preparedStatement.setString(12,obj.getWeekday());
             preparedStatement.executeUpdate();

             DBConnexion.getCon().commit();
             DBConnexion.getCon().setAutoCommit(true);

         }
         catch (SQLException e){
             try {
                 DBConnexion.getCon().rollback();
             } catch (SQLException ex) {
                 System.err.println(ex);
             }
             System.err.println(e);
         }
    }

    @Override
    public boolean delete(Site obj) {
        try {
            DBConnexion.getCon().setAutoCommit(false);
            PreparedStatement sql= DBConnexion.getCon().prepareStatement("DELETE FROM site where id=?");

            sql.setInt(1,obj.getId());

            sql.executeUpdate();

            DBConnexion.getCon().commit();

            DBConnexion.getCon().setAutoCommit(true);

            return true;

        }catch (Exception e){
            try{
                DBConnexion.getCon().rollback();
            }catch (SQLException ex){
                System.err.println(ex);
                return  false;
            }
            System.err.println(e);
            return false;
        }
    }
    public boolean deleteMultiple(Collection<Site> sites)
    {
        try {
            DBConnexion.getCon().setAutoCommit(false);
            for (Site site: sites) {
                PreparedStatement sql= DBConnexion.getCon().prepareStatement("DELETE FROM site where id=?");
                sql.setInt(1,site.getId());
                sql.executeUpdate();
            }
            DBConnexion.getCon().commit();
            DBConnexion.getCon().setAutoCommit(true);
            return true;
        } catch (Exception e) {
            try {
                DBConnexion.getCon().rollback();
            } catch (SQLException ex) {
                System.err.println(ex);
                return  false;
            }
            System.err.println(e);
            return false;
        }
    }
    public boolean existance(int id) {
        try {
            PreparedStatement sql= null;
            sql = DBConnexion.getCon().prepareStatement("SELECT * From site where id=?");
            sql.setInt(1,id);
            ResultSet set= sql.executeQuery();
            if(set.next())return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public int getDureeVisiteBySiteId(int siteId) {
        int dureeVisite = 0;
        try {
            Connection connection = DBConnexion.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT DureViste FROM site WHERE id = ?");
            preparedStatement.setInt(1, siteId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                dureeVisite = resultSet.getInt("DureViste");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dureeVisite;
    }


}