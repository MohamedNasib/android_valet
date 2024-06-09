package com.icapp.icapp.Models.Output.AddSignature;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("front_image_date")
	private Long frontImageDate;

	@SerializedName("created_at")
	private long createdAt;

	@SerializedName("area_id")
	private int areaId;

	@SerializedName("right_image_date")
	private Long rightImageDate;

	@SerializedName("updated_at")
	private long updatedAt;

	@SerializedName("left_image_date")
	private Long leftImageDate;

	@SerializedName("submit_later")
	private int submitLater;

	@SerializedName("id")
	private int id;

	@SerializedName("right_image")
	private String rightImage;

	@SerializedName("issues_photos")
	private List<IssuesPhotosItem> issuesPhotos;

	@SerializedName("longitude")
	private String longitude;

	@SerializedName("back_image_date")
	private Long backImageDate;

	@SerializedName("address")
	private String address;

	@SerializedName("user_image")
	private String userImage;

	@SerializedName("signature_image")
	private String signatureImage;

	@SerializedName("ticket_number")
	private String ticketNumber;

	@SerializedName("front_image")
	private String frontImage;

	@SerializedName("back_image")
	private String backImage;

	@SerializedName("pdf")
	private String pdf;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("plate_number")
	private String plateNumber;

	@SerializedName("additional_photos")
	private List<AdditionalPhotosItem> additionalPhotos;

	@SerializedName("left_image")
	private String leftImage;

	@SerializedName("status")
	private int status;

	@SerializedName("username")
	private String username;

	public String getLatitude(){
		return latitude;
	}

	public Long getFrontImageDate(){
		return frontImageDate;
	}

	public long getCreatedAt(){
		return createdAt;
	}

	public int getAreaId(){
		return areaId;
	}

	public Long getRightImageDate(){
		return rightImageDate;
	}

	public long getUpdatedAt(){
		return updatedAt;
	}

	public Long getLeftImageDate(){
		return leftImageDate;
	}

	public int getSubmitLater(){
		return submitLater;
	}

	public int getId(){
		return id;
	}

	public String getRightImage(){
		return rightImage;
	}

	public List<IssuesPhotosItem> getIssuesPhotos(){
		return issuesPhotos;
	}

	public String getLongitude(){
		return longitude;
	}

	public Long getBackImageDate(){
		return backImageDate;
	}

	public String getAddress(){
		return address;
	}

	public String getUserImage(){
		return userImage;
	}

	public String getSignatureImage(){
		return signatureImage;
	}

	public String getTicketNumber(){
		return ticketNumber;
	}

	public String getFrontImage(){
		return frontImage;
	}

	public String getBackImage(){
		return backImage;
	}

	public String getPdf(){
		return pdf;
	}

	public int getUserId(){
		return userId;
	}

	public String getPlateNumber(){
		return plateNumber;
	}

	public List<AdditionalPhotosItem> getAdditionalPhotos(){
		return additionalPhotos;
	}

	public String getLeftImage(){
		return leftImage;
	}

	public int getStatus(){
		return status;
	}

	public String getUsername(){
		return username;
	}
}