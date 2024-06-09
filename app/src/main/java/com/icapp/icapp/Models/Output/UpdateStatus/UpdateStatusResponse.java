package com.icapp.icapp.Models.Output.UpdateStatus;

import com.google.gson.annotations.SerializedName;

public class UpdateStatusResponse{

	@SerializedName("data")
	private Data data;

	public Data getData(){
		return data;
	}
}