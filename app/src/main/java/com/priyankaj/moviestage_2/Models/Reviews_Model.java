package com.priyankaj.moviestage_2.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Reviews_Model implements Parcelable {
    public static final Creator<Reviews_Model> CREATOR = new Creator<Reviews_Model>() {
        public Reviews_Model createFromParcel(Parcel source) {
            return new Reviews_Model(source);
        }

        @Override
        public Reviews_Model[] newArray(int size) {
            return new Reviews_Model[size];
        }
    };
    String author;
    String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Reviews_Model() {
    }

    public Reviews_Model(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }
}
