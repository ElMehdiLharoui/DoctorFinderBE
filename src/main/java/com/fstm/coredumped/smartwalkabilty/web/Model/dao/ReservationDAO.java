package com.fstm.coredumped.smartwalkabilty.web.Model.dao;

import com.fstm.coredumped.smartwalkabilty.common.model.bo.Reservation;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Annonce;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Image;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Site;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class ReservationDAO implements IDAO<Reservation>{
    private static ReservationDAO daoReservation=null;
    public static ReservationDAO getDaoReservation(){
        if(daoReservation==null)daoReservation=new ReservationDAO();
        return daoReservation;
    }
    private ReservationDAO(){

    }
        @Override
    public boolean Create(Reservation obj) {
        return false;
    }

    @Override
    public Collection<Reservation> Retrieve() {
      System.out.println("wahwah1");
        return null;
    }

    @Override
    public void update(Reservation obj)
    {
        try {
            Connexion.getCon().setAutoCommit(false);
            PreparedStatement preparedStatement= Connexion.getCon().prepareStatement("UPDATE  reservation SET status=? where id=?");
            preparedStatement.setString(1,obj.getStatus());
            preparedStatement.setInt(2,obj.getId());
            preparedStatement.executeUpdate();
            Connexion.getCon().commit();
            Connexion.getCon().setAutoCommit(true);
        }catch (SQLException e){
            try {
                Connexion.getCon().rollback();
            } catch (SQLException ex) {
                System.err.println(ex);
            }
            System.err.println(e);
        }

    }
    public void updateMontant(Reservation obj)
    {
        try {
            Connexion.getCon().setAutoCommit(false);
            PreparedStatement preparedStatement= Connexion.getCon().prepareStatement("UPDATE  reservation SET montant=? where id=?");
            preparedStatement.setFloat(1,obj.getMontant());
            preparedStatement.setInt(2,obj.getId());
            preparedStatement.executeUpdate();
            Connexion.getCon().commit();
            Connexion.getCon().setAutoCommit(true);
        }catch (SQLException e){
            try {
                Connexion.getCon().rollback();
            } catch (SQLException ex) {
                System.err.println(ex);
            }
            System.err.println(e);
        }

    }
    @Override
    public boolean delete(Reservation obj) {
        return false;
    }
    public Collection<Reservation> getReservationBySiteAndDate(int siteId,  Date date) {
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation = null;
        try {
            Connection connection = Connexion.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM reservation WHERE idSite = ?  AND date = ?");
            preparedStatement.setInt(1, siteId);
            preparedStatement.setDate(2, date);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reservation = extractReservation(resultSet);
                reservations.add(reservation);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private Reservation extractReservation(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(resultSet.getInt("id"));
        reservation.setDate(resultSet.getDate("date"));
        reservation.setTempDeReservation(resultSet.getString("tempDeReservation"));
        reservation.setCreatedAt(resultSet.getTimestamp("createdAt"));
        reservation.setCIN(resultSet.getString("CIN"));
        reservation.setlName(resultSet.getString("lName"));
        reservation.setfName(resultSet.getString("fName"));
        reservation.setStatus(resultSet.getString("status"));
        reservation.setMontant(resultSet.getFloat("montant"));
        reservation.setNumberInLine(resultSet.getInt("NumberInLine"));
        reservation.setTelephone_number(resultSet.getString("telephone_number"));
        return reservation;
    }

    public boolean checkExiste(int id) {
        try {
            PreparedStatement sql= null;
            sql = Connexion.getCon().prepareStatement("SELECT * From reservation where id=?");
            sql.setInt(1,id);
            ResultSet set= sql.executeQuery();
            if(set.next())return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public Collection<Reservation> ReservationBySite(int idSite){
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation = null;
        try{
            PreparedStatement sql =null;
            sql= Connexion.getCon().prepareStatement("select numberinline, tempdereservation FROM reservation where idsite = ? ");
            sql.setInt(1,idSite);
            ResultSet resultSet = sql.executeQuery();
            while (resultSet.next()) {
                reservation = extractReservation(resultSet);
                reservations.add(reservation);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return reservations;
    }

}
