package com.icapp.icapp.Models.Output.CheckCompany;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("code")
	private String code;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	public String getCode(){
		return code;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}
}