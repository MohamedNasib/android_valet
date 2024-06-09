package com.icapp.icapp.Models.Output.Settings;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("code")
	private String code;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("users")
	private List<UsersItem> users;

	public String getCode(){
		return code;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public List<UsersItem> getUsers(){
		return users;
	}
}