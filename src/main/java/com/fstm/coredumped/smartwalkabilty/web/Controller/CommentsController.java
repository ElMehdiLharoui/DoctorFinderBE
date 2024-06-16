package com.fstm.coredumped.smartwalkabilty.web.Controller;

import com.fstm.coredumped.smartwalkabilty.common.model.service.CommentsService;
import com.fstm.coredumped.smartwalkabilty.common.model.service.response.BasicResponse;
import com.fstm.coredumped.smartwalkabilty.common.model.service.response.GetCommentsResponse;
import com.fstm.coredumped.smartwalkabilty.web.Model.Service.JWTgv;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentsController extends HttpServlet {

    public static final String APPLICATION_JSON = "application/json";
    private final CommentsService service = new CommentsService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(APPLICATION_JSON);
        try {
            String authorization = request.getHeader("Authorization");

            if (!new JWTgv().verifyToken(authorization)) {
                response.getWriter().println("{ \"mes\":\" invalid Token  \" }");
                return;
            }

            int siteId = Integer.parseInt(request.getParameter("siteId"));

            BasicResponse<GetCommentsResponse> response1 = service.getSiteComments(siteId);
            response.getWriter().println(gson.toJson(response1));

            if (!response1.isSuccess())
                response.setStatus(400);
        } catch (Exception e) {
            System.err.println(e);
            response.setStatus(400);
            response.getWriter().println("{ \"mes\":\"  Exception Happened \" }");
        }
    }
}
