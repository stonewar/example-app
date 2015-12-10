package com.stonewar.appname.activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.drawable.AnimationDrawable;
import com.stonewar.appname.R;
import com.stonewar.appname.common.AbstractBaseActivity;
import com.stonewar.appname.common.AbstractViewPagerFragment;
import com.stonewar.appname.adapter.ViewPagerAdapter;
import com.stonewar.appname.common.IRowViewPagerInteractionListener;
import com.stonewar.appname.fragment.MediaPlayerFragment;
import com.stonewar.appname.fragment.PlayBackFragment;
import com.stonewar.appname.googlecode.SlidingTabLayout;
import com.stonewar.appname.manager.FragmentManager;
import com.stonewar.appname.model.Song;
import com.stonewar.appname.service.IMediaPlayerService;
import com.stonewar.appname.util.AppMediaPlayer;
import com.stonewar.appname.util.Constant;
import com.stonewar.appname.util.Factory;
import com.stonewar.appname.util.Util;
import java.util.List;

public class Main2Activity extends AbstractBaseActivity implements IRowViewPagerInteractionListener,
        ServiceConnection, PlayBackFragment.IPlayBackFragmentInteractionListener, MediaPlayerFragment.IMediaPlayerFragmentInteractionListener {

    private static final String TAG = Main2Activity.class.getName();

    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private PlayBackFragment playBackFragment;
    private LinearLayout playBackFragmentContainer;

    private MediaPlayerFragment mediaPlayerFragment;

    //Service
    private IMediaPlayerService playerService;
    public boolean isServiceBound;

    private int currentSongPosition;
    private int playingTimeInterval;
    private int stoppingTimeInterval;
    private List<Song> selectedSongs;

    private Song songToPlay;
    private Handler playerHandler;
    private ImageView lastSelectedEqualizer;

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

        playBackFragmentContainer = (LinearLayout) findViewById(R.id.play_back_fragment_container);
        playBackFragment = (PlayBackFragment) getFragmentManager().findFragmentById(R.id.play_back_Fragment);


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
                    if(mediaPlayerFragment != null) {
                        if (!isSongDurationSet) {
                            mediaPlayerFragment.setSongDuration((int) finalTime);
                            isSongDurationSet = true;
                        }
                        mediaPlayerFragment.setSongProgress((int) timeElapsed);
                    }

                } else if (action.equals(Constant.ACTION_SONG_CHANGE)) {
                    if(mediaPlayerFragment != null) {
                        mediaPlayerFragment.setSong((Song) bundle.getParcelable(Constant.PLAYING_SONG));
                        isSongDurationSet = false;
                    }
                } else {
                    updatePlaybackButton(action);
                    if(mediaPlayerFragment != null) {
                        mediaPlayerFragment.updatePlaybackButton(action);
                    }
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
    public void selectedView(View v, Song song) {
        songToPlay = song;
        playerService.stop();
        playerService.setCurrentSong(songToPlay);
        playBackFragmentContainer.setVisibility(View.VISIBLE);
        playBackFragment.getArtwork().setImageBitmap(songToPlay.getArtWork());
        playBackFragment.getTitle().setText(songToPlay.getTitle());
        playBackFragment.getAuthor().setText(songToPlay.getAuthor());

        ImageView equalizer = (ImageView) v.findViewById(R.id.tab_title_equalizer_image);
        if (this.lastSelectedEqualizer != null) {
            ((AnimationDrawable)this.lastSelectedEqualizer.getBackground()).stop();
            this.lastSelectedEqualizer.setVisibility(View.GONE);
        }
        equalizer.setVisibility(View.VISIBLE);
        equalizer.setBackgroundResource(R.drawable.ic_equalizer_white_36dp);
        equalizer.getBackground().setTint(Color.parseColor("#3F51B5"));
        ((AnimationDrawable) equalizer.getBackground()).start();
        this.lastSelectedEqualizer = equalizer;
        onPlayBackButtonClicked();
    }

    @Override
    public void onPlayBackButtonClicked() {
        final int mediaPlayerState = playerService.getMediaPlayerState();
        boolean toPlay;
        if (mediaPlayerState != AppMediaPlayer.STATE_STARTED) {
            playerService.play();
            playBackFragment.getPlayback().setImageBitmap(Util.getBitmap(this, R.mipmap.uamp_ic_pause_white_24dp));
            toPlay = true;
        } else {
            playerService.pause();
            playBackFragment.getPlayback().setImageBitmap(Util.getBitmap(this, R.mipmap.uamp_ic_play_arrow_white_24dp));
            toPlay = false;
        }
        updateEqualizer(toPlay);
    }

    @Override
    public void onViewClicked(View v) {
        tabs.setVisibility(View.GONE);
        pager.setVisibility(View.GONE);
        playBackFragmentContainer.setVisibility(View.GONE);
        findViewById(R.id.media_player_fragment).setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.GONE);
        if(mediaPlayerFragment == null) {
            mediaPlayerFragment = MediaPlayerFragment.newInstance(null, null);
            FragmentManager.replaceFragment(R.id.media_player_fragment, mediaPlayerFragment, getFragmentManager(), R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO
    }

    public void updatePlaybackButton(String action) {
        boolean toPlay;
        if (action.equals(Constant.ACTION_SONG_PAUSE)) {
            playBackFragment.getPlayback().setImageBitmap(Util.getBitmap(this, R.mipmap.uamp_ic_play_arrow_white_24dp));
            toPlay = false;
        } else {
            playBackFragment.getPlayback().setImageBitmap(Util.getBitmap(this, R.mipmap.uamp_ic_pause_white_24dp));
            toPlay = true;
        }
        updateEqualizer(toPlay);
    }

    public void updateEqualizer(boolean toPlay) {
        if (toPlay) {
            lastSelectedEqualizer.setBackgroundResource(R.drawable.ic_equalizer_white_36dp);
            lastSelectedEqualizer.getBackground().setTint(Color.parseColor("#3F51B5"));
            ((AnimationDrawable) lastSelectedEqualizer.getBackground()).start();
        } else {
            lastSelectedEqualizer.setBackgroundResource(R.drawable.ic_equalizer_white_36dp);
            lastSelectedEqualizer.getBackground().setTint(Color.GRAY);
            ((AnimationDrawable) lastSelectedEqualizer.getBackground()).stop();
        }
    }

    @Override
    public void onBackPressed() {
        tabs.setVisibility(View.VISIBLE);
        pager.setVisibility(View.VISIBLE);
        playBackFragmentContainer.setVisibility(View.VISIBLE);
        findViewById(R.id.media_player_fragment).setVisibility(View.GONE);
//        super.onBackPressed();
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
