package com.icapp.icapp.Models.Output.AddSignature;

import com.google.gson.annotations.SerializedName;

public class AddSignatureResponse{

	@SerializedName("data")
	private Data data;

	public Data getData(){
		return data;
	}
}