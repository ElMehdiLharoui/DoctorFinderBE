package com.fstm.coredumped.smartwalkabilty.routing.model.bo;

import java.util.List;
import java.util.Map;

public interface IAlgo
{
    List<Chemin> doAlgo(Graph graph, GeoPoint depart, GeoPoint arr);
    Chemin doAlgo(Graph graph, GeoPoint depart, GeoPoint arr,GeoPoint inter); //algo to pass by a specific point
    static Chemin Construct_Chemin(Map<GeoPoint,GeoPoint> interChemin,Graph G,GeoPoint arr)
    {
        Chemin chemin =new Chemin();
        GeoPoint ge=arr;
        while (true)
        {
            GeoPoint another=interChemin.get(ge);
            if(another==null)break;
            chemin.Add_Route(G.findVertex(another,ge));
            ge=another;
        }
        return chemin;
    }
}
