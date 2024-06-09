package com.icapp.icapp.Models.Output.Arrivals;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ArrivalsResponse{

	@SerializedName("data")
	private List<DataItem> data;

	public List<DataItem> getData(){
		return data;
	}
}