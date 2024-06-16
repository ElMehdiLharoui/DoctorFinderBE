package com.fstm.coredumped.smartwalkabilty.common.model.service;

import com.fstm.coredumped.smartwalkabilty.common.model.bo.Comment;
import com.fstm.coredumped.smartwalkabilty.common.model.dao.DAOComments;
import com.fstm.coredumped.smartwalkabilty.common.model.service.response.BasicResponse;
import com.fstm.coredumped.smartwalkabilty.common.model.service.response.GetCommentsResponse;

import java.util.Collection;
import java.util.List;
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

    public BasicResponse<GetCommentsResponse> getSiteComments(int siteId) {
        try {
            GetCommentsResponse response = new GetCommentsResponse();
            List<Comment> commentsBySiteId = dao.getCommentsBySiteId(siteId);
            response.setAverageRating(commentsBySiteId.stream().map(Comment::getRating).reduce(Double::sum).orElse(0D) / commentsBySiteId.size());
            response.setComments(commentsBySiteId);

            return BasicResponse.success(response);
        }catch (Exception e) {
            return BasicResponse.fail(e.getMessage());
        }
    }
}
