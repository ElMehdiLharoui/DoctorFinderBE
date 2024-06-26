package com.fstm.coredumped.smartwalkabilty.core.routing.model.dao;


import com.fstm.coredumped.smartwalkabilty.common.model.bo.GeoPoint;
import com.fstm.coredumped.smartwalkabilty.core.routing.model.bo.Graph;
import com.fstm.coredumped.smartwalkabilty.core.routing.model.bo.Vertex;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DAOGraph implements IDAOGraph {
    @Override
    public Graph getTheGraph(GeoPoint source, GeoPoint target){
        return getTheGraph(source,target,2.5);
    }


    public Graph getTheGraph(GeoPoint source, GeoPoint target, double d) {
        Graph graph = new Graph();
        // calculate distance:

        double distance = Math.sqrt(
                Math.pow((source.getLongtitude() - target.getLongtitude()), 2)+
                        Math.pow((source.getLaltittude() - target.getLaltittude()), 2)
        );
        // calculate the radius
        double Radius = (distance/2.0)*d;

        // calculate midPoint
        double Ycenter = (source.getLaltittude() + target.getLaltittude())/2.0;
        double Xcenter = (source.getLongtitude() + target.getLongtitude())/2.0;

        // set a projection system
        int projection_system = 4326;

        //Edges
        int edges = 2;

        // specify the_geom
        String geom = "the_geom";
        try
        {
            Connection c = OSMDBConnexion.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement("select gid as id, source, target, length_m, x1, y1, x2, y2 FROM ways WHERE st_contains((\n" +
                    "SELECT ST_BUFFER (\n" +
                    "\tST_SetSRID(ST_Point(?, ?),?)\n" +
                    "\t, ?, ?\n" +
                    ")), the_geom);");

            preparedStatement.setDouble(1, Xcenter);
            preparedStatement.setDouble(2, Ycenter);

            preparedStatement.setInt(3, projection_system);

            // let the distance for now then we can use the Radius
            preparedStatement.setDouble(4, Radius);
            preparedStatement.setInt(5, edges);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Vertex v = new Vertex();
                v.setId(resultSet.getInt("id"));
                v.setDepart(new GeoPoint(resultSet.getInt("source"), resultSet.getDouble("y1"), resultSet.getDouble("x1")));
                v.setArrive(new GeoPoint(resultSet.getInt("target"), resultSet.getDouble("y2"), resultSet.getDouble("x2")));
                v.setDistance(resultSet.getDouble("length_m"));
                v.setRisk(0);
                // v.setRisk(resultSet.getInt("risk"));
                graph.Add_Route(v);
            }

            if (!graph.contains(target)){
                System.out.println("Graph not containes the target");
                graph.addPoint(target, null,0);
            }

            if(!graph.contains(source)){
                System.out.println("Graph not contains the source ");
                graph.addPoint(source, target,Radius);
            }
            if(graph.isConnected(source,target))
                return graph;
            System.err.println("Graph is not connected");
            return  new DAOGraph().getTheGraph(source,target,d*2);
        }catch (Exception e){
            System.out.println("Err in graph creation: "+e);
            e.printStackTrace();
            return graph;
        }
    }
}
//-7.5794811 ,33.5411488 => -7.6319347, 33.5170524