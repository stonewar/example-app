package com.stonewar.appname.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by yandypiedra on 10.11.15.
 */
public class Song implements Parcelable {

    private Long id;
    private Bitmap artWork;
    private String title;
    private String author;
    private boolean isSelected;

    public Song(Long id, Bitmap image, String songTitle, String songAuthor, boolean isSelected) {
        this(id, image, songTitle, songAuthor);
        this.isSelected = isSelected;
    }

    public Song(Long id, Bitmap image, String songTitle, String songAuthor) {
        this.id = id;
        this.artWork = image;
        this.title = songTitle;
        this.author = songAuthor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bitmap getArtWork() {
        return artWork;
    }

    public void setArtWork(Bitmap artWork) {
        this.artWork = artWork;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    protected Song(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        artWork = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        title = in.readString();
        author = in.readString();
        isSelected = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(id);
        }
        dest.writeValue(artWork);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
