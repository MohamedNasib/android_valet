package com.icapp.icapp.Models.Output.CheckCompany;

import com.google.gson.annotations.SerializedName;

public class CheckCompanyResponse {

	@SerializedName("data")
	private Data data;

	public Data getData(){
		return data;
	}

	@SerializedName("message")
	private String message;

	public String getMessage(){
		return message;
	}
}