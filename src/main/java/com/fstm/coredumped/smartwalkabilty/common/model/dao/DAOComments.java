package com.fstm.coredumped.smartwalkabilty.common.model.dao;

import com.fstm.coredumped.smartwalkabilty.common.model.bo.Comment;
import com.fstm.coredumped.smartwalkabilty.web.Model.dao.DBConnexion;
import com.fstm.coredumped.smartwalkabilty.web.Model.dao.IDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class DAOComments implements IDAO<Comment> {
    private static DAOComments daoComment = null;

    public static DAOComments getDAOComment() {
        if (daoComment == null) daoComment = new DAOComments();
        return daoComment;
    }

    public DAOComments() {
    }

    @Override
    public boolean Create(Comment obj) {
        try {
            Connection connection = DBConnexion.getCon();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Comment (rating, comment, idSite, idUser, createdAt) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            fillStatementRequest(obj, preparedStatement);
            preparedStatement.executeUpdate();
            ResultSet set = preparedStatement.getGeneratedKeys();

            if (set.next()) {
                obj.setId(set.getInt(1));
            }

            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try {
                DBConnexion.getCon().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    private void fillStatementRequest(Comment comment, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setDouble(1, comment.getRating());
        preparedStatement.setString(2, comment.getComment());
        preparedStatement.setInt(3, comment.getIdSite());
        preparedStatement.setString(4, comment.getIdUser());
        preparedStatement.setTimestamp(5, new Timestamp(comment.getCreatedAt().getTime()));
    }

    @Override
    public Collection<Comment> Retrieve() {
        Collection<Comment> comments = new ArrayList<>();
        try {
            Connection connection = DBConnexion.getCon();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Comment");

            while (resultSet.next()) {
                Comment comment = extractComment(resultSet);
                comments.add(comment);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public Collection<Comment> getCommentsBySiteId(int siteId) {
        Collection<Comment> comments = new ArrayList<>();
        try {
            Connection connection = DBConnexion.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Comment WHERE idSite = ?");
            preparedStatement.setInt(1, siteId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Comment comment = extractComment(resultSet);
                comments.add(comment);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    @Override
    public void update(Comment obj) {
        try {
            Connection connection = DBConnexion.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Comment SET rating = ?, comment = ?, idSite = ?, idUser = ?, createdAt = ? WHERE id = ?"
            );

            fillStatementRequest(obj, preparedStatement);
            preparedStatement.setInt(6, obj.getId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(Comment obj) {
        try {
            Connection connection = DBConnexion.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Comment WHERE id = ?");
            preparedStatement.setInt(1, obj.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Comment extractComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getInt("id"));
        comment.setRating(resultSet.getDouble("rating"));
        comment.setComment(resultSet.getString("comment"));
        comment.setIdSite(resultSet.getInt("idSite"));
        comment.setIdUser(resultSet.getString("idUser"));
        comment.setCreatedAt(resultSet.getTimestamp("createdAt"));
        return comment;
    }


    public Optional<Comment> getCommentByUserIdAndSiteId(String userId, int siteId) {
        Collection<Comment> comments = new ArrayList<>();
        try {
            Connection connection = DBConnexion.getCon();
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Comment WHERE idUser = ? AND idSite = ?")) {
                preparedStatement.setString(1, userId);
                preparedStatement.setInt(2, siteId);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Comment comment = extractComment(resultSet);
                    comments.add(comment);
                }

                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments.stream().findFirst();
    }
}
