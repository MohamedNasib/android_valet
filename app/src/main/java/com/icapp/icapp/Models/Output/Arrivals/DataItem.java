package com.icapp.icapp.Models.Output.Arrivals;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.icapp.icapp.Models.Output.AddSignature.IssuesPhotosItem;

public class DataItem{

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("front_image_date")
	private long frontImageDate;

	@SerializedName("created_at")
	private long createdAt;

	@SerializedName("area_id")
	private int areaId;

	@SerializedName("updates")
	private List<Object> updates;

	@SerializedName("another_left_image")
	private String anotherLeftImage;

	@SerializedName("right_image_date")
	private long rightImageDate;

	@SerializedName("updated_at")
	private long updatedAt;

	@SerializedName("left_image_date")
	private long leftImageDate;

	@SerializedName("another_front_image")
	private String anotherFrontImage;

	@SerializedName("submit_later")
	private int submitLater;

	@SerializedName("another_back_image")
	private String anotherBackImage;

	@SerializedName("id")
	private int id;

	@SerializedName("right_image")
	private String rightImage;

	@SerializedName("issues_photos")
	private List<IssuesPhotosItem> issuesPhotos;

	@SerializedName("another_left_image_date")
	private long anotherLeftImageDate;

	@SerializedName("longitude")
	private String longitude;

	@SerializedName("back_image_date")
	private long backImageDate;

	@SerializedName("address")
	private String address;

	@SerializedName("user_image")
	private String userImage;

	@SerializedName("signature_image")
	private String signatureImage;

	@SerializedName("ticket_number")
	private String ticketNumber;

	@SerializedName("another_back_image_date")
	private long anotherBackImageDate;

	@SerializedName("front_image")
	private String frontImage;

	@SerializedName("another_front_image_date")
	private long anotherFrontImageDate;

	@SerializedName("back_image")
	private String backImage;

	@SerializedName("pdf")
	private String pdf;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("another_right_image")
	private String anotherRightImage;

	@SerializedName("plate_number")
	private String plateNumber;

	@SerializedName("another_right_image_date")
	private long anotherRightImageDate;

	@SerializedName("additional_photos")
	private List<AdditionalPhotosItem> additionalPhotos;

	@SerializedName("valet")
	private Valet valet;

	@SerializedName("left_image")
	private String leftImage;

	@SerializedName("status")
	private int status;

	@SerializedName("username")
	private String username;

	public String getLatitude(){
		return latitude;
	}

	public long getFrontImageDate(){
		return frontImageDate;
	}

	public long getCreatedAt(){
		return createdAt;
	}

	public int getAreaId(){
		return areaId;
	}

	public List<Object> getUpdates(){
		return updates;
	}

	public String getAnotherLeftImage(){
		return anotherLeftImage;
	}

	public long getRightImageDate(){
		return rightImageDate;
	}

	public long getUpdatedAt(){
		return updatedAt;
	}

	public long getLeftImageDate(){
		return leftImageDate;
	}

	public String getAnotherFrontImage(){
		return anotherFrontImage;
	}

	public int getSubmitLater(){
		return submitLater;
	}

	public String getAnotherBackImage(){
		return anotherBackImage;
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

	public long getAnotherLeftImageDate(){
		return anotherLeftImageDate;
	}

	public String getLongitude(){
		return longitude;
	}

	public long getBackImageDate(){
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

	public long getAnotherBackImageDate(){
		return anotherBackImageDate;
	}

	public String getFrontImage(){
		return frontImage;
	}

	public long getAnotherFrontImageDate(){
		return anotherFrontImageDate;
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

	public String getAnotherRightImage(){
		return anotherRightImage;
	}

	public String getPlateNumber(){
		return plateNumber;
	}

	public long getAnotherRightImageDate(){
		return anotherRightImageDate;
	}

	public List<AdditionalPhotosItem> getAdditionalPhotos(){
		return additionalPhotos;
	}

	public Valet getValet(){
		return valet;
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