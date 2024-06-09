package com.icapp.icapp.Models.Output.Failure;

import com.google.gson.annotations.SerializedName;

public class NotFoundResponse{

	@SerializedName("message")
	private String message;

	public String getMessage(){
		return message;
	}
}