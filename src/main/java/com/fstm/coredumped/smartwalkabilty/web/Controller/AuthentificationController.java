package com.fstm.coredumped.smartwalkabilty.web.Controller;

import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.LoginResponseDTO;
import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.organisatioDTO;
import com.fstm.coredumped.smartwalkabilty.web.Model.Service.JWTgv;
import com.fstm.coredumped.smartwalkabilty.web.Model.Service.MD5Hash;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Organisation;
import com.fstm.coredumped.smartwalkabilty.web.Model.dao.DAOOrganisation;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class AuthentificationController extends HttpServlet {
    public static class LoginBlob {
        String login;
        String password;

        LoginBlob() {
        }
    }

    public static class err {
        String error;

        err(String errMessage) {
            this.error = errMessage;
        }
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);
        super.service(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Gson gson = new Gson();
        Optional<LoginBlob> blob = Optional.ofNullable(gson.fromJson(req.getReader(), LoginBlob.class));
        if (blob.isPresent()) {
            String generatedPassword = MD5Hash.md5Hash(blob.get().password);
            Organisation organisation = new DAOOrganisation().authentification(blob.get().login, generatedPassword);
            if (organisation != null) {
                LoginResponseDTO dto = generateAndReturnToken(organisation);
                resp.getWriter().println(gson.toJson(dto));
                return;
            }
        }

        resp.setStatus(400);
        resp.getWriter().println(gson.toJson(new err("Authentication Error : no account found with the provided login/password")));
    }

    public static LoginResponseDTO generateAndReturnToken(Organisation organisation) {
        String token = new JWTgv().generateToken(organisation);
        return new LoginResponseDTO(token, new organisatioDTO(
                organisation.getId(),
                organisation.getNom(),
                // formatter.format(organisation.getDateCreation()),
                organisation.getLogin(),
                organisation.getEmail()
        ));
    }

    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Max-Age", "10");
    }
}
