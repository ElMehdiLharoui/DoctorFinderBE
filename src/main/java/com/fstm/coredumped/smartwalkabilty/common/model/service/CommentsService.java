package com.fstm.coredumped.smartwalkabilty.common.model.service;

import com.fstm.coredumped.smartwalkabilty.common.model.bo.Comment;
import com.fstm.coredumped.smartwalkabilty.common.model.dao.DAOComments;
import com.fstm.coredumped.smartwalkabilty.common.model.service.response.BasicResponse;

import java.util.Optional;

public class CommentsService {

    private final DAOComments dao;

    public CommentsService() {
        dao = DAOComments.getDAOComment();
    }

    public BasicResponse<Integer> comment(String commentStr, double rating, int idSite, String idUser) {
        if (rating > 5 || rating <= 0) {
            return BasicResponse.fail("Rating must be between 0 and 5");
        }

        Optional<Comment> commentOptional = dao.getCommentByUserIdAndSiteId(idUser, idSite);
        if (commentOptional.isPresent()) {
            return BasicResponse.fail("Comment already exists");
        }

        Comment comment = new Comment(rating, commentStr, idSite, idUser);
        if (dao.Create(comment))
            return BasicResponse.success(comment.getId());

        return BasicResponse.fail("Comment creation failed");
    }
}
