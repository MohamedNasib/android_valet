package com.icapp.icapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CarUpdate extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
//    private Integer status;
    private Long createdAt;
    private Long updatedAt;
//    private String plateNumber;
    private String ticketNumber;
//    private String front_image;
//    private Long front_date;
//    private String left_image;
//    private Long left_date;
//    private String back_image;
//    private Long back_date;
//    private String right_image;
//    private Long right_date;
    private String damages;
//    private String additional;
//    private String signature;
    private String user_id;
//    private String user_name;
//    private String user_image;
//    private Double latitude;
//    private Double longitude;
//    private String address;
//    private Integer submit_later; // 1 for now, 2 for later, 3 for never
//    private Integer require_full_sync;
//    private Integer require_sign_sync;
//    private Integer require_status_sync;
//    private String pdf;
    private String ticket_id;

    public CarUpdate() {
        this.id = "_" + UUID.randomUUID().toString();
//        this.status = ORDER_STATUS_0;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
//        this.latitude = 0.0;
//        this.longitude = 0.0;
//        this.address = ".";
//        this.submit_later = 2;
//        this.require_full_sync = 1;
//        this.require_sign_sync = 0;
//        this.require_status_sync = 0;
//        this.pdf = "";
    }

    public CarUpdate(String ticketNumber, String damages, String user_id, String ticket_id) {
        this.id = "_" + UUID.randomUUID().toString();
//        this.status = status;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
//        this.plateNumber = plateNumber;
        this.ticketNumber = ticketNumber;
//        this.front_image = front_image;
//        this.front_date = front_date;
//        this.left_image = left_image;
//        this.left_date = left_date;
//        this.back_image = back_image;
//        this.back_date = back_date;
//        this.right_image = right_image;
//        this.right_date = right_date;
        this.damages = damages;
//        this.additional = additional;
//        this.signature = signature;
        this.user_id = user_id;
//        this.user_name = user_name;
//        this.user_image = user_image;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.address = address;
//        this.submit_later = submit_later;
//        this.require_full_sync = require_full_sync;
//        this.require_sign_sync = require_sign_sync;
//        this.require_status_sync = require_status_sync;
//        this.pdf = pdf;
        this.ticket_id = ticket_id;
    }

    public String getId() {
        return id;
    }

//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

//    public String getPlateNumber() {
//        return plateNumber;
//    }
//
//    public void setPlateNumber(String plateNumber) {
//        this.plateNumber = plateNumber;
//    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

//    public String getFront_image() {
//        return front_image;
//    }
//
//    public void setFront_image(String front_image) {
//        this.front_image = front_image;
//    }
//
//    public Long getFront_date() {
//        return front_date;
//    }
//
//    public void setFront_date(Long front_date) {
//        this.front_date = front_date;
//    }
//
//    public String getLeft_image() {
//        return left_image;
//    }
//
//    public void setLeft_image(String left_image) {
//        this.left_image = left_image;
//    }
//
//    public Long getLeft_date() {
//        return left_date;
//    }
//
//    public void setLeft_date(Long left_date) {
//        this.left_date = left_date;
//    }
//
//    public String getBack_image() {
//        return back_image;
//    }
//
//    public void setBack_image(String back_image) {
//        this.back_image = back_image;
//    }
//
//    public Long getBack_date() {
//        return back_date;
//    }
//
//    public void setBack_date(Long back_date) {
//        this.back_date = back_date;
//    }
//
//    public String getRight_image() {
//        return right_image;
//    }
//
//    public void setRight_image(String right_image) {
//        this.right_image = right_image;
//    }
//
//    public Long getRight_date() {
//        return right_date;
//    }
//
//    public void setRight_date(Long right_date) {
//        this.right_date = right_date;
//    }

    public String getDamages() {
        return damages == null ? "" : damages;
    }

    public void setDamages(String damages) {
        this.damages = damages;
    }

//    public String getAdditional() {
//        return additional == null ? "" : additional;
//    }
//
//    public void setAdditional(String additional) {
//        this.additional = additional;
//    }
//
//    public String getSignature() {
//        return signature;
//    }
//
//    public void setSignature(String signature) {
//        this.signature = signature;
//    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

//    public String getUser_name() {
//        return user_name;
//    }
//
//    public void setUser_name(String user_name) {
//        this.user_name = user_name;
//    }
//
//    public String getUser_image() {
//        return user_image;
//    }
//
//    public void setUser_image(String user_image) {
//        this.user_image = user_image;
//    }
//
//    public Double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(Double latitude) {
//        this.latitude = latitude;
//    }
//
//    public Double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(Double longitude) {
//        this.longitude = longitude;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public Integer getSubmit_later() {
//        return submit_later;
//    }
//
//    public void setSubmit_later(Integer submit_later) {
//        this.submit_later = submit_later;
//    }
//
//    public Integer getRequire_full_sync() {
//        return require_full_sync;
//    }
//
//    public void setRequire_full_sync(Integer require_full_sync) {
//        this.require_full_sync = require_full_sync;
//    }
//
//    public Integer getRequire_sign_sync() {
//        return require_sign_sync;
//    }
//
//    public void setRequire_sign_sync(Integer require_sign_sync) {
//        this.require_sign_sync = require_sign_sync;
//    }
//
//    public Integer getRequire_status_sync() {
//        return require_status_sync;
//    }
//
//    public void setRequire_status_sync(Integer require_status_sync) {
//        this.require_status_sync = require_status_sync;
//    }
//
//    public String getPdf() {
//        return pdf;
//    }
//
//    public void setPdf(String pdf) {
//        this.pdf = pdf;
//    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
//        dest.writeInt(this.status);
        dest.writeLong(this.createdAt);
        dest.writeLong(this.updatedAt);
//        dest.writeString(this.plateNumber);
        dest.writeString(this.ticketNumber);
//        dest.writeString(this.front_image);
//        dest.writeLong(this.front_date);
//        dest.writeString(this.left_image);
//        dest.writeLong(this.left_date);
//        dest.writeString(this.back_image);
//        dest.writeLong(this.back_date);
//        dest.writeString(this.right_image);
//        dest.writeLong(this.right_date);
        dest.writeString(this.damages);
//        dest.writeString(this.additional);
//        dest.writeString(this.signature);
        dest.writeString(this.user_id);
//        dest.writeString(this.user_name);
//        dest.writeString(this.user_image);
//        dest.writeDouble(this.latitude);
//        dest.writeDouble(this.longitude);
//        dest.writeString(this.address);
//        dest.writeInt(this.submit_later);
//        dest.writeInt(this.require_full_sync);
//        dest.writeInt(this.require_sign_sync);
//        dest.writeInt(this.require_status_sync);
//        dest.writeString(this.pdf);
        dest.writeString(this.ticket_id);
    }

    protected CarUpdate(Parcel in) {
        this.id = in.readString();
//        this.status = in.readInt();
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
//        this.plateNumber = in.readString();
        this.ticketNumber = in.readString();
//        this.front_image = in.readString();
//        this.front_date = in.readLong();
//        this.left_image = in.readString();
//        this.left_date = in.readLong();
//        this.back_image = in.readString();
//        this.back_date = in.readLong();
//        this.right_image = in.readString();
//        this.right_date = in.readLong();
        this.damages = in.readString();
//        this.additional = in.readString();
//        this.signature = in.readString();
        this.user_id = in.readString();
//        this.user_name = in.readString();
//        this.user_image = in.readString();
//        this.latitude = in.readDouble();
//        this.longitude = in.readDouble();
//        this.address = in.readString();
//        this.submit_later = in.readInt();
//        this.require_full_sync = in.readInt();
//        this.require_sign_sync = in.readInt();
//        this.require_status_sync = in.readInt();
//        this.pdf = in.readString();
        this.ticket_id = in.readString();
    }

    public static final Creator<CarUpdate> CREATOR = new Creator<CarUpdate>() {
        @Override
        public CarUpdate createFromParcel(Parcel source) {
            return new CarUpdate(source);
        }

        @Override
        public CarUpdate[] newArray(int size) {
            return new CarUpdate[size];
        }
    };
}