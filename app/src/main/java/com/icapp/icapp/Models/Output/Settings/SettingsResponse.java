package com.icapp.icapp.Models.Output.Settings;

import com.google.gson.annotations.SerializedName;

public class SettingsResponse{

	@SerializedName("data")
	private Data data;

	public Data getData(){
		return data;
	}
}