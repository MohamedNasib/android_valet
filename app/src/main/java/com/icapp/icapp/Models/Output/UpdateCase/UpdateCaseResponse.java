package com.icapp.icapp.Models.Output.UpdateCase;

import com.google.gson.annotations.SerializedName;

public record UpdateCaseResponse(

	@SerializedName("message")
	String message
) {
}