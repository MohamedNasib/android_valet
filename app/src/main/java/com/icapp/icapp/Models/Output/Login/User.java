package com.icapp.icapp.Models.Output.Login;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id") int id ;
    @SerializedName("first_name") String first_name ;
    @SerializedName("last_name") String last_name ;
    @SerializedName("company_id") int company_id ;
    @SerializedName("area_id") int area_id ;
    @SerializedName("pass_code") String pass_code ;
    @SerializedName("face_image") String face_image ;
    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getCompany_id() {
        return company_id;
    }

    public int getArea_id() {
        return area_id;
    }

    public String getPass_code() {
        return pass_code;
    }

    public String getFace_image() {
        return face_image;
    }
}
