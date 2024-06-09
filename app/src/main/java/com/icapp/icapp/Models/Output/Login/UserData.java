package com.icapp.icapp.Models.Output.Login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserData {
    @SerializedName("data")
    User data;
    @SerializedName("message")
    String message;
    @SerializedName("errors")
    List<UserRequierment> errors;

    public User getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public List<UserRequierment> getErrors() {
        return errors;
    }
}

class UserRequierment{
    @SerializedName("company_code")
    String company_code;
    @SerializedName("pass_code")
    String pass_code;
    public String getCompany_code() {
        return company_code;
    }

    public String getPass_code() {
        return pass_code;
    }
}
