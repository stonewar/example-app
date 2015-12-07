package com.stonewar.appname.common;

import android.graphics.Bitmap;

import com.stonewar.appname.model.Song;

/**
 * Created by yandypiedra on 25.11.15.
 */
public interface IMediaPlayerController {

    void play();

    void pause();

    void next();

    void previous();

    boolean isPlaying();

    void stop();

//    void setSongDuration(int finalTime);
//
//    void setSongProgress(int timeElapsed);
//
//    void updatePlaybackButton(String action);
//
//    void setSong(Song song);

//    void setSongDuration(int songDuration);
//
//    void setSongProgress(int timeElapsed);

    void seekTo(int pos);

//  int getBufferPercentage();
//
//  boolean canPause();
}
