package com.icapp.icapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject implements Parcelable {

    @PrimaryKey
    private String id;
    private String firstName;
    private String lastName;
    private int companyId;
    private int areaId;
    private String passCode;
    private String faceImage;

    public User() {
        this.id = UUID.randomUUID().toString();
    }
    public User(String id, String firstName, String lastName, int companyId, int areaId, String passCode, String faceImage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyId = companyId;
        this.areaId = areaId;
        this.passCode = passCode;
        this.faceImage = faceImage;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeValue(this.firstName);
        dest.writeString(this.lastName);
        dest.writeInt(this.companyId);
        dest.writeInt(this.areaId);
        dest.writeValue(this.passCode);
        dest.writeString(this.faceImage);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.firstName = source.readString();
        this.lastName = source.readString();
        this.companyId = source.readInt();
        this.areaId = source.readInt();
        this.passCode = source.readString();
        this.faceImage = source.readString();
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.companyId = in.readInt();
        this.areaId = in.readInt();
        this.passCode = in.readString();
        this.faceImage = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
