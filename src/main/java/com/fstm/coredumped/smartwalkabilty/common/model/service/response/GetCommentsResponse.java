package com.fstm.coredumped.smartwalkabilty.common.model.service.response;

import com.fstm.coredumped.smartwalkabilty.common.model.bo.Comment;

import java.util.List;

public class GetCommentsResponse {
    private double averageRating;
    private List<Comment> comments;

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
