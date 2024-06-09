package com.icapp.icapp.Models.Output.Arrival;

import com.google.gson.annotations.SerializedName;

public class ArrivalResponse{

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	public Data getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}
}