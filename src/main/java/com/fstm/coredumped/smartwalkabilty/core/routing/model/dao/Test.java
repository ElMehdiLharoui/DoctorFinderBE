package com.fstm.coredumped.smartwalkabilty.core.routing.model.dao;

import com.fstm.coredumped.smartwalkabilty.common.model.bo.GeoPoint;
import com.fstm.coredumped.smartwalkabilty.core.routing.model.bo.Chemin;
import com.fstm.coredumped.smartwalkabilty.core.routing.model.bo.Dijkistra;
import com.fstm.coredumped.smartwalkabilty.core.routing.model.bo.Graph;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        GeoPoint p1 = new GeoPoint(-7.640186, 33.5368971);
        GeoPoint p2 = new GeoPoint(-7.5708861, 33.5611801);

        Graph graph = new DAOGraph().getTheGraph(p1, p2);

        List<Chemin> chemins = new Dijkistra().doAlgo(graph, p1, p2);


        System.out.println(chemins);
    }
}