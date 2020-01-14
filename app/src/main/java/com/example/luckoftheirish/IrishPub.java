package com.example.luckoftheirish;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class IrishPub implements Parcelable {

    public LatLng latLng;
    public String name;
    public String photoURL;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public IrishPub(LatLng latLng, String name, String photoURL){
        this.latLng = latLng;
        this.name = name;
        this.photoURL = photoURL;
    }


    protected IrishPub(Parcel in) {
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        name = in.readString();
        photoURL = in.readString();
    }

    public static final Creator<IrishPub> CREATOR = new Creator<IrishPub>() {
        @Override
        public IrishPub createFromParcel(Parcel in) {
            return new IrishPub(in);
        }

        @Override
        public IrishPub[] newArray(int size) {
            return new IrishPub[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(photoURL);
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
