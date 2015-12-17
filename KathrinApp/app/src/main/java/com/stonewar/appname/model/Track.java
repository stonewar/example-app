package com.stonewar.appname.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yandypiedra on 10.11.15.
 */

public class Track implements Parcelable {

    private Long id;
    private Bitmap artWork;
    private String title;
    private String author;
    private String albumTitle;
    private Long duration;
    private boolean isSelected;

    public Track(Long id, Bitmap image, String songTitle, String songAuthor, String albumTitle, Long duration, boolean isSelected) {
        this(id, image, songTitle, songAuthor, albumTitle, duration);
        this.isSelected = isSelected;
    }

    public Track(Long id, Bitmap image, String songTitle, String songAuthor, String albumTitle, Long duration) {
        this.id = id;
        this.artWork = image;
        this.title = songTitle;
        this.author = songAuthor;
        this.albumTitle = albumTitle;
        this.duration = duration;
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

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    protected Track(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        artWork = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        title = in.readString();
        author = in.readString();
        albumTitle = in.readString();
        duration = in.readByte() == 0x00 ? null : in.readLong();
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
        dest.writeString(albumTitle);
        if (duration == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(duration);
        }
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}