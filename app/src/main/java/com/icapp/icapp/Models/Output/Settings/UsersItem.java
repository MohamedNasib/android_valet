package com.icapp.icapp.Models.Output.Settings;

import com.google.gson.annotations.SerializedName;

public class UsersItem{

	@SerializedName("company_id")
	private int companyId;

	@SerializedName("face_image")
	private String faceImage;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("id")
	private int id;

	@SerializedName("area_id")
	private int areaId;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("pass_code")
	private String passCode;

	public int getCompanyId(){
		return companyId;
	}

	public String getFaceImage(){
		return faceImage;
	}

	public String getLastName(){
		return lastName;
	}

	public int getId(){
		return id;
	}

	public int getAreaId(){
		return areaId;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getPassCode(){
		return passCode;
	}
}