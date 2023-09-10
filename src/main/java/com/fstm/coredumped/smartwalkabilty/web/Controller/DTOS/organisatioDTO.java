package com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS;

public class organisatioDTO {
    int id;
    String name;
   // String datecreated;
    String login;
    String email;

    public organisatioDTO(int id, String name,  String login, String email) {
        this.id = id;
        this.name = name;
      //  this.datecreated = datecreated;
        this.login = login;
        this.email = email;
    }
}
