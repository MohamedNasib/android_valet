package com.icapp.icapp.Models.Output.UpdateStatus;

import com.google.gson.annotations.SerializedName;

public class IssuesPhotosItem{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("ticket_id")
	private int ticketId;

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public int getTicketId(){
		return ticketId;
	}
}