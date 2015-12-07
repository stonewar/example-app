package com.stonewar.appname.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.stonewar.appname.R;
import com.stonewar.appname.common.AbstractBaseActivity;
import com.stonewar.appname.common.AbstractViewPagerFragment;
import com.stonewar.appname.adapter.ViewPagerAdapter;
import com.stonewar.appname.common.ISongCallBack;
import com.stonewar.appname.fragment.PlayBackFragment;
import com.stonewar.appname.googlecode.SlidingTabLayout;
import com.stonewar.appname.model.Song;
import com.stonewar.appname.service.IMediaPlayerService;
import com.stonewar.appname.util.AppMediaPlayer;
import com.stonewar.appname.util.Constant;
import com.stonewar.appname.util.Factory;
import com.stonewar.appname.util.Util;

import java.util.List;

public class Main2Activity extends AbstractBaseActivity implements ISongCallBack, ServiceConnection, PlayBackFragment.IPlayBackButtonListener {

    private static final String TAG = Main2Activity.class.getName();

    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private PlayBackFragment playBackFragment;
    private LinearLayout playBackfragmentContainer;

    //Service
    private IMediaPlayerService playerService;
    public boolean isServiceBound;

    private int currentSongPosition;
    private int playingTimeInterval;
    private int stoppingTimeInterval;
    private List<Song> selectedSongs;

    private Song songToPlay;
    private Handler playerHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AbstractViewPagerFragment[] viewPagerFragments = new AbstractViewPagerFragment[3];
        for (int i = 0; i < viewPagerFragments.length; i++)
            viewPagerFragments[i] = Factory.createViewPagerFragment(i + 1);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), viewPagerFragments);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(adapter);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        playBackfragmentContainer = (LinearLayout) findViewById(R.id.play_back_fragment_container);
        playBackFragment = (PlayBackFragment) getFragmentManager().findFragmentById(R.id.play_back_Fragment);


        playerHandler = new Handler() {
            private boolean isSongDurationSet;

            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String action = bundle.getString(Constant.ACTION);
                Log.d(TAG, "Action: " + action);

                if (action.equals(Constant.ACTION_UPDATE_SEEK_BAR)) {
//                    double timeElapsed = bundle.getDouble(Constant.TIME_ELAPSED);
//                    double finalTime = bundle.getDouble(Constant.SONG_DURATION);
//                    if (!isSongDurationSet) {
//                        setSongDuration((int) finalTime);
//                        isSongDurationSet = true;
//                    }
//                    setSongProgress((int) timeElapsed);

                } else if (action.equals(Constant.ACTION_SONG_CHANGE)) {
//                    selectedSong((Song) bundle.getParcelable(Constant.PLAYING_SONG));
//                    isSongDurationSet = false;
                } else {
//
                }
            }
        };


        //TODO get the selected songs
        //this.selectedSongs = ...
    }

    @Override
    public int contentView() {
        return R.layout.activity_main2;
    }

    @Override
    public void selectedSong(Song song) {
        songToPlay = song;
        playerService.stop();
        playerService.setCurrentSong(songToPlay);
        playBackfragmentContainer.setVisibility(View.VISIBLE);
        playBackFragment.getArtwork().setImageBitmap(songToPlay.getArtWork());
        playBackFragment.getTitle().setText(songToPlay.getTitle());
        playBackFragment.getAuthor().setText(songToPlay.getAuthor());
        playBackButtonClicked();
    }

    @Override
    public void playBackButtonClicked() {
        final int mediaPlayerState = playerService.getMediaPlayerState();
        if (mediaPlayerState != AppMediaPlayer.STATE_STARTED) {
            playerService.play();
            playBackFragment.getPlayback().setImageBitmap(Util.getBitmap(this, R.mipmap.uamp_ic_pause_white_24dp));
        } else {
            playerService.pause();
            playBackFragment.getPlayback().setImageBitmap(Util.getBitmap(this, R.mipmap.uamp_ic_play_arrow_white_24dp));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to Service
        if (!isServiceBound) {
            Intent serviceIntent = new Intent(this, IMediaPlayerService.class);
            bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
            startService(serviceIntent);
        }
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
        IMediaPlayerService.PlayerServiceBinder binder = (IMediaPlayerService.PlayerServiceBinder) service;
        playerService = binder.getService();
        //TODO interval
        playerService.setStoppingTimeInterval(5);
        playerService.setPlayingTimeInterval(10);
        playerService.setSelectedSongs(selectedSongs);
        playerService.setCurrentSong(songToPlay);
        playerService.setCurrentSongPosition(currentSongPosition);
        playerService.setHandler(playerHandler);
        isServiceBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        playerService = null;
        isServiceBound = false;
    }
}
