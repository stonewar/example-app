package com.stonewar.appname.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.stonewar.appname.R;
import com.stonewar.appname.fragment.MediaPlayerFragment;
import com.stonewar.appname.model.Track;
import com.stonewar.appname.service.MediaPlayerService;
import com.stonewar.appname.util.AppMediaPlayer;
import com.stonewar.appname.util.Constant;
import com.stonewar.appname.util.Factory;
import com.stonewar.appname.util.Util;

import java.util.List;

/**
 * Created by yandypiedra on 02.01.16.
 */
public abstract class AbstractMediaPlayerActivity extends AbstractBaseActivity
    implements ServiceConnection, MediaPlayerFragment.IMediaPlayerFragmentInteractionListener {

    private static final String TAG = AbstractMediaPlayerActivity.class.getName();

    //Service
    protected MediaPlayerService playerService;
    protected boolean isServiceBound;

    protected int currentTrackPosition;
    protected int playingTimeInterval;
    protected int stoppingTimeInterval;

    protected Handler playerHandler;
    protected MediaPlayerFragment mediaPlayerFragment;
    protected Track currentTrack;
    protected List<Track> selectedTracks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playingTimeInterval = getIntent().getExtras().getInt(Constant.PLAYING_TIME_INTERVAL);
        stoppingTimeInterval = getIntent().getExtras().getInt(Constant.STOPPING_TIME_INTERVAL);

        AbstractViewPagerFragment[] viewPagerFragments = new AbstractViewPagerFragment[3];
        for (int i = 0; i < viewPagerFragments.length; i++)
            viewPagerFragments[i] = Factory.createViewPagerFragment(i + 1);

//        playBackFragment = (PlayBackFragment) getFragmentManager().findFragmentById(R.id.play_back_Fragment);
        mediaPlayerFragment = (MediaPlayerFragment) getFragmentManager().findFragmentById(R.id.play_back_Fragment);

        playerHandler = new Handler() {
            private boolean isSongDurationSet;

            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();

                String action = bundle.getString(Constant.ACTION);
                Log.d(TAG, "Action: " + action);

                if (action.equals(Constant.ACTION_UPDATE_SEEK_BAR)) {
                    double timeElapsed = bundle.getDouble(Constant.TIME_ELAPSED);
                    double finalTime = bundle.getDouble(Constant.SONG_DURATION);
                    if (mediaPlayerFragment != null) {
//                        if (!isSongDurationSet) {
                        mediaPlayerFragment.setSongDuration((int) finalTime);
//                            isSongDurationSet = true;
//                        }
                        mediaPlayerFragment.setSongProgress((int) timeElapsed);
                    }

                } else if (action.equals(Constant.ACTION_SONG_CHANGE)) {
                    if (mediaPlayerFragment != null) {
                        mediaPlayerFragment.setTrack((Track) bundle.getParcelable(Constant.PLAYING_TRACK));
//                        isSongDurationSet = false;
                    }
                } else {
                    updatePlaybackButton(action);
                }
            }
        };
    }

    @Override
    public abstract int contentView();

    @Override
    public abstract void onPLayLayoutClicked(View v);

    public abstract void updateEqualizer(boolean toPlay);

    @Override
    public void onPlayBackButtonClicked() {
        play();
    }

    @Override
    public void play() {
        final int mediaPlayerState = playerService.getMediaPlayerState();
        final String action;
        if (mediaPlayerState != AppMediaPlayer.STATE_STARTED) {
            action = Constant.ACTION_SONG_STARTED;
            playerService.play();
        } else {
            mediaPlayerFragment.updatePlaybackButton(Constant.ACTION_SONG_PAUSE);
            action = Constant.ACTION_SONG_PAUSE;
            playerService.pause();
        }

        if (mediaPlayerFragment != null) {
            mediaPlayerFragment.updatePlaybackButton(action);
        }

        updatePlaybackButton(action);
    }

    public void updatePlaybackButton(String action) {
        final boolean toPlay;
        if (action.equals(Constant.ACTION_SONG_PAUSE)) {
            mediaPlayerFragment.getPlayback().setImageBitmap(Util.getBitmap(this, R.mipmap.uamp_ic_play_arrow_white_24dp));
            toPlay = false;
        } else {
            mediaPlayerFragment.getPlayback().setImageBitmap(Util.getBitmap(this, R.mipmap.uamp_ic_pause_white_24dp));
            toPlay = true;
        }

        if (mediaPlayerFragment != null) {
            Log.d(TAG, "Updating mediaPlayerFragment playBackButton");
            mediaPlayerFragment.updatePlaybackButton(action);
        }
        updateEqualizer(toPlay);
    }

    @Override
    public void next() {
        playerService.next();
    }

    @Override
    public void previous() {
        playerService.previous();
    }

    @Override
    public void seekTo(int progress) {
        playerService.seekTo(progress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to Service
        Intent serviceIntent = new Intent(this, MediaPlayerService.class);
        bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
        startService(serviceIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (isServiceBound) {
            unbindService(this);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MediaPlayerService.PlayerServiceBinder binder = (MediaPlayerService.PlayerServiceBinder) service;
        playerService = binder.getService();
        playerService.setStoppingTimeInterval(stoppingTimeInterval);
        playerService.setPlayingTimeInterval(playingTimeInterval);
        playerService.setSelectedTracks(selectedTracks);
        playerService.setCurrentTrack(currentTrack);
        playerService.setCurrentTrackPosition(currentTrackPosition);
        playerService.setHandler(playerHandler);
        isServiceBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        playerService = null;
        isServiceBound = false;
    }
}
