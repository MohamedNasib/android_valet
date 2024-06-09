package com.icapp.icapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class CarPart extends RealmObject implements Parcelable {
    private String path;
    private String name;
    private Long date;

    public CarPart() {
    }

    public CarPart(String path, String name, Long date) {
        this.path = path;
        this.name = name;
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.name);
        dest.writeValue(this.date);
    }

    public void readFromParcel(Parcel source) {
        this.path = source.readString();
        this.name = source.readString();
        this.date = (Long) source.readValue(Long.class.getClassLoader());
    }

    protected CarPart(Parcel in) {
        this.path = in.readString();
        this.name = in.readString();
        this.date = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<CarPart> CREATOR = new Creator<CarPart>() {
        @Override
        public CarPart createFromParcel(Parcel source) {
            return new CarPart(source);
        }

        @Override
        public CarPart[] newArray(int size) {
            return new CarPart[size];
        }
    };
}
