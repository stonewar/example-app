package com.stonewar.appname.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yandypiedra on 17.12.15.
 */
public class Album implements Parcelable {

    private Long id;
    private List<Track> trackList;
    private String title;
    private String artist;

    public Album(List<Track> trackList, Long id, String title, String artist) {
        this.trackList = trackList;
        this.id = id;
        this.title = title;
        this.artist = artist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    protected Album(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        if (in.readByte() == 0x01) {
            trackList = new ArrayList<>();
            in.readList(trackList, Track.class.getClassLoader());
        } else {
            trackList = null;
        }
        title = in.readString();
        artist = in.readString();
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
        if (trackList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(trackList);
        }
        dest.writeString(title);
        dest.writeString(artist);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}