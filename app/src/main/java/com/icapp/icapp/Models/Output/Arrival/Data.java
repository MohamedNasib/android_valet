package com.icapp.icapp.Models.Output.Arrival;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("report")
	private String report;

	public String getReport(){
		return report;
	}
}