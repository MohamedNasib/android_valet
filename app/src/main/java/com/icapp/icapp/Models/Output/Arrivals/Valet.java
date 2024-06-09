package com.icapp.icapp.Models.Output.Arrivals;

import com.google.gson.annotations.SerializedName;

public class Valet{

	@SerializedName("company_id")
	private int companyId;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("id")
	private int id;

	@SerializedName("area_id")
	private int areaId;

	@SerializedName("first_name")
	private String firstName;

	public int getCompanyId(){
		return companyId;
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
}