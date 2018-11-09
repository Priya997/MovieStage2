package com.priyankaj.moviestage_2.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailers_Model implements Parcelable {
    String name,key;

    public static final Creator<Trailers_Model> CREATOR = new Creator<Trailers_Model>() {
        public Trailers_Model createFromParcel(Parcel source) {
            return new Trailers_Model(source);
        }

        @Override
        public Trailers_Model[] newArray(int size) {
            return new Trailers_Model[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Trailers_Model() {
    }

    public Trailers_Model(Parcel in) {
        name = in.readString();
        key = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
    }
}
