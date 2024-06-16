package com.fstm.coredumped.smartwalkabilty.common.controller;

import com.fstm.coredumped.smartwalkabilty.common.model.service.response.BasicResponse;
import com.fstm.coredumped.smartwalkabilty.common.model.service.CommentsService;
import com.fstm.coredumped.smartwalkabilty.common.model.service.response.GetCommentsResponse;

public class CommentsController {
    private final CommentsService service;

    public CommentsController() {
        service = new CommentsService();
    }

    public BasicResponse<Integer> comment(CommentRequest commentRequest) {
        try {
            return service.comment(commentRequest.getComment(), commentRequest.getRating(), commentRequest.getIdSite(), commentRequest.getIdUser());
        } catch (Exception e) {
            e.printStackTrace();
            return BasicResponse.fail(e.getMessage());
        }
    }

    public BasicResponse<GetCommentsResponse> getComments(GetCommentsRequest request) {
        try {
            return service.getSiteComments(request.getSiteId());
        } catch (Exception e) {
            e.printStackTrace();
            return BasicResponse.fail(e.getMessage());
        }
    }
}
