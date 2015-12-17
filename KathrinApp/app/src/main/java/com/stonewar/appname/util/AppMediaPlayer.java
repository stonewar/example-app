package com.stonewar.appname.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by yandypiedra on 19.11.15.
 */
public class AppMediaPlayer extends MediaPlayer {

    private static final String TAG = AppMediaPlayer.class.getName();

    public static final int STATE_IDLE = 0;
    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_PREPARING = 2;
    public static final int STATE_PREPARED = 3;
    public static final int STATE_STARTED = 4;
    public static final int STATE_PAUSED = 5;
    public static final int STATE_STOPPED = 6;
    public static final int STATE_PLAYBACK = 7;
    public static final int STATE_END = 8;
    public static final int STATE_ERROR = 9;

    private int currentState = STATE_IDLE;

    @Override
    public void setDataSource(Context context, Uri contentUri) throws IOException {
        if(currentState == STATE_IDLE) {
            setAudioStreamType(AudioManager.STREAM_MUSIC);
            super.setDataSource(context, contentUri);
            currentState = STATE_INITIALIZED;
        }
    }

    @Override
    public void prepareAsync(){
        if(currentState == STATE_INITIALIZED || currentState == STATE_STOPPED){
            super.prepareAsync();
            currentState = STATE_PREPARING;
        }
    }

    @Override
    public void prepare() throws IOException {
        if(currentState == STATE_INITIALIZED || currentState == STATE_STOPPED){
            super.prepare();
            currentState = STATE_PREPARED;
        }
    }

    @Override
    public void start(){
        if(currentState == STATE_PREPARED || currentState == STATE_PAUSED || currentState == STATE_PLAYBACK) {
        super.start();
            currentState = STATE_STARTED;
        }
    }

    @Override
    public void pause(){
        if(currentState == STATE_STARTED) {
            super.pause();
            currentState = STATE_PAUSED;
        }
    }

    @Override
    public void stop(){
        if(currentState == STATE_PREPARED || currentState == STATE_PAUSED || currentState == STATE_PLAYBACK || currentState == STATE_STARTED) {
            super.stop();
            currentState = STATE_STOPPED;
        }
    }

    public void playBack(){
        if(currentState == STATE_STARTED) {
            //TODO
            currentState = STATE_PLAYBACK;
        }
    }

    @Override
    public void release(){
        super.release();
        currentState = STATE_END;
    }

    @Override
    public void reset(){
        super.reset();
        currentState = STATE_IDLE;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public int getCurrentState() {
        return currentState;
    }
}
