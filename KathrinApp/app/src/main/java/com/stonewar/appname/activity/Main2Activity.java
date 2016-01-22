package com.stonewar.appname.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.drawable.AnimationDrawable;

import com.stonewar.appname.R;
import com.stonewar.appname.common.AbstractMediaPlayerActivity;
import com.stonewar.appname.common.AbstractViewPagerFragment;
import com.stonewar.appname.adapter.ViewPagerAdapter;
import com.stonewar.appname.common.IRowViewPagerInteractionListener;
import com.stonewar.appname.googlecode.SlidingTabLayout;
import com.stonewar.appname.model.Album;
import com.stonewar.appname.model.Track;
import com.stonewar.appname.util.Constant;
import com.stonewar.appname.util.Factory;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AbstractMediaPlayerActivity implements IRowViewPagerInteractionListener {

    private static final String TAG = Main2Activity.class.getName();

    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private LinearLayout playBackFragmentContainer;
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
    }

    @Override
    public int contentView() {
        return R.layout.activity_main2;
    }

    @Override
    public void onPLayLayoutClicked(View v) {
        tabs.setVisibility(View.GONE);
        pager.setVisibility(View.GONE);
        mediaPlayerFragment.setTrack(currentTrack);
        mediaPlayerFragment.showMediaPlayerLayout();

    }

    @Override
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
    public void selectedTrack(View v, Track song, List<Track> tracks) {
        currentTrack = song;
        playerService.stop();
        playerService.setCurrentTrack(currentTrack);
        selectedTracks = tracks;
        playerService.setSelectedTracks(selectedTracks);
        playBackFragmentContainer.setVisibility(View.VISIBLE);
        mediaPlayerFragment.setTrack(song);

        ImageView equalizer = (ImageView) v.findViewById(R.id.tab_title_equalizer_image);
        if (this.lastSelectedEqualizer != null) {
            ((AnimationDrawable) this.lastSelectedEqualizer.getBackground()).stop();
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
    public void selectedAlbum(View v, Album album) {

        ImageView albumImage = (ImageView) v.findViewById(R.id.tab_album_image_song);
        List<Track> tracks = album.getTrackList();
        for (Track t : tracks)
            t.setArtWork(null);

        Intent intent = new Intent(this, AlbumArtistActivity.class);
        intent.putExtra(Constant.ALBUM, album);

        if (currentTrack != null) {
            currentTrack.setArtWork(null);
        }

        for (Track t : selectedTracks)
            t.setArtWork(null);

        intent.putExtra(Constant.PLAYING_TRACK, currentTrack);
        intent.putParcelableArrayListExtra(Constant.SELECTED_TRACKS, (ArrayList<? extends Parcelable>) selectedTracks);
        intent.putExtra(Constant.PLAYING_TIME_INTERVAL, playingTimeInterval);
        intent.putExtra(Constant.STOPPING_TIME_INTERVAL, stoppingTimeInterval);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, albumImage, "photo");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void selectedArtist(View v, List<Track> songs) {
        //TODO
    }

    @Override
    public void onBackPressed() {
        tabs.setVisibility(View.VISIBLE);
        pager.setVisibility(View.VISIBLE);
        mediaPlayerFragment.showPlayBackLayout();
//        super.onBackPressed();
    }
}
