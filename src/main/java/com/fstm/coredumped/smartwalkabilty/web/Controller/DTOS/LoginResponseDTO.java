package com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS;

public class LoginResponseDTO {
    String token;
    organisatioDTO organisation;
    public LoginResponseDTO(String t, organisatioDTO org){
        this.organisation = org;
        this.token = t;
    }
}
