package com.fstm.coredumped.smartwalkabilty.common.model.dao;

import com.fstm.coredumped.smartwalkabilty.common.model.bo.Reservation;
import com.fstm.coredumped.smartwalkabilty.web.Model.dao.Connexion;
import com.fstm.coredumped.smartwalkabilty.web.Model.dao.IDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class DAOReservation implements IDAO<Reservation> {
    private static DAOReservation daoReservation = null;

    public static DAOReservation getDAOResrvation() {
        if (daoReservation == null) daoReservation = new DAOReservation();
        return daoReservation;
    }

    public DAOReservation() {
    }


    @Override
    public boolean Create(Reservation obj) {
        try {
            Connexion.getCon().setAutoCommit(false);
            PreparedStatement preparedStatement = Connexion.getCon().prepareStatement(
                    "INSERT INTO Reservation (date, tempDeReservation, createdAt, CIN, lName, fName, status, montant, NumberInLine, idSite,telephone_number) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            fillStatementRequest(obj, preparedStatement);
            preparedStatement.executeUpdate();
            ResultSet set = preparedStatement.getGeneratedKeys();

            if (set.next()) {
                obj.setId(set.getInt(1));
            }

            Connexion.getCon().commit();
            Connexion.getCon().setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try {
                Connexion.getCon().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
               return false;
            }
            e.printStackTrace();
            return false;
        }
    }

    public Reservation getReservationBySiteAndCINAndDate(int siteId, String cin,Date date)
    {
        Reservation reservation = null;
        try {
            Connection connection = Connexion.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reservation WHERE idSite = ? and cin = ? and date = ?");
            preparedStatement.setInt(1, siteId);
            preparedStatement.setString(2, cin);
            preparedStatement.setDate(3, date);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                reservation = extractReservation(resultSet);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }





    private void fillStatementRequest(Reservation reservation, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setDate(1, new Date(reservation.getDate().getTime()));
        preparedStatement.setString(4, reservation.getCIN());
        preparedStatement.setString(5, reservation.getlName());
        preparedStatement.setString(6, reservation.getfName());
        preparedStatement.setInt(10, reservation.getSite().getId());
        preparedStatement.setString(2, reservation.getTempDeReservation());
        preparedStatement.setDate(3, new Date(reservation.getCreatedAt().getTime())); // Utilise la date actuelle pour le champ created_at
        preparedStatement.setString(7, reservation.getStatus());
        preparedStatement.setFloat(8, reservation.getMontant());
        preparedStatement.setInt(9, reservation.getNumberInLine());
        preparedStatement.setString(11,reservation.getTelephone_number());
    }

    public int countReservationsByDateAndSite(Date date, int idSite) {
        int count = 0;
        try {
            Connection connection = Connexion.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM reservation WHERE date = ? and idSite = ?");
            preparedStatement.setDate(1, new java.sql.Date(date.getTime())); // Conversion nécessaire ici
            preparedStatement.setInt(2, idSite);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public Collection<Reservation> getReservationsBySiteId(int siteId) {
        Collection<Reservation> reservations = new ArrayList<>();
        Reservation reservation = null;
        try {
            Connection connection = Connexion.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reservation WHERE idSite = ?");
            preparedStatement.setInt(1, siteId);

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
        return reservation;
    }

    @Override
    public Collection<Reservation> Retrieve() {
        return null;
    }

    @Override
    public void update(Reservation obj) {

    }

    public boolean updateMontant(int reservationId, float newMontant) {
        try {
            Connection connection = Connexion.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE reservation SET montant = ? and status = ? WHERE id = ?");
            preparedStatement.setFloat(1, newMontant);
            preparedStatement.setInt(3, reservationId);
            preparedStatement.setString(2, "Payed");

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Reservation obj) {
        try {
            Connection connection = Connexion.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM reservation WHERE id = ?");
            preparedStatement.setInt(1, obj.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
