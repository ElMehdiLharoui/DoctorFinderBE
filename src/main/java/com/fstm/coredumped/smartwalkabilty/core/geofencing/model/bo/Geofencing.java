package com.fstm.coredumped.smartwalkabilty.core.geofencing.model.bo;

import com.fstm.coredumped.smartwalkabilty.common.model.bo.GeoPoint;
import com.fstm.coredumped.smartwalkabilty.core.geofencing.model.dao.DAOGAnnonce;
import com.fstm.coredumped.smartwalkabilty.core.routing.model.bo.Chemin;
import com.fstm.coredumped.smartwalkabilty.core.routing.model.bo.Observer;
import com.fstm.coredumped.smartwalkabilty.core.routing.model.bo.Routage;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Site;
import com.fstm.coredumped.smartwalkabilty.web.Model.dao.DAOSite;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Geofencing implements Observer{

    Routage routing;
    double radius;
    List<Integer> cats;
    @Override
    public void update() {
        findAllAnnonces();
    }

    public Geofencing(Routage routnig, double radius,List<Integer> cats){
        this.routing = routnig;
        this.radius = radius;
        this.cats=cats;
    }

    void findAllAnnonces(){
        DAOGAnnonce daoga = new DAOGAnnonce();
        for (Chemin c : routing.getChemins()){
            try {
                Set<Integer> ids = daoga.getSitesOfChemin(c,this.radius);
                for (Integer id : ids) {
                    c.getSites().add(DAOSite.getDaoSite().findById(id,cats));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public static List<Site> findAllAnnoncesByRadius(GeoPoint point, double radius,List<Integer> cats){
        List<Site> listSites = new ArrayList<>();
        DAOGAnnonce daoga = new DAOGAnnonce();
            try {
                Set<Integer> ids = daoga.getSitesOfPoint(point,radius);
                for (Integer id : ids) {
                    listSites.add(DAOSite.getDaoSite().findById(id,cats));
                }
                return listSites;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return null;
            }
    }
}
