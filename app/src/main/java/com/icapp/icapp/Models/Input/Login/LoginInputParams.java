package com.icapp.icapp.Models.Input.Login;

public class LoginInputParams {
    String company_code ;
    String pass_code ;
    public LoginInputParams(String company_code, String pass_code) {
        this.company_code = company_code;
        this.pass_code = pass_code;
    }
}
