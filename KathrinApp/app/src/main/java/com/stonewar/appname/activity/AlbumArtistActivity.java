package com.stonewar.appname.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.stonewar.appname.R;
import com.stonewar.appname.adapter.AlbumArtistAdapter;
import com.stonewar.appname.common.AbstractMediaPlayerActivity;
import com.stonewar.appname.fragment.MediaPlayerFragment;
import com.stonewar.appname.googlecode.DividerItemDecoration;
import com.stonewar.appname.manager.TrackManager;
import com.stonewar.appname.model.Album;
import com.stonewar.appname.model.Track;
import com.stonewar.appname.util.Constant;

public class AlbumArtistActivity extends AbstractMediaPlayerActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout playBackFragmentContainer;
    private CoordinatorLayout albumArtistLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        albumArtistLayout = (CoordinatorLayout) findViewById(R.id.album_artist);
        playBackFragmentContainer = (LinearLayout) findViewById(R.id.play_back_fragment_container);

        Album album = getIntent().getParcelableExtra(Constant.ALBUM);
        ImageView artistOrArtworkImg = (ImageView) findViewById(R.id.album_or_author_image);
        Bitmap artistOrArtwork = TrackManager.findArtworkById(getContentResolver(), this, album.getTrackList().get(0).getId());
        artistOrArtworkImg.setImageBitmap(artistOrArtwork);

        songToPlay = getIntent().getParcelableExtra(Constant.PLAYING_SONG);

        if (songToPlay != null) {
            playBackFragmentContainer.setVisibility(View.VISIBLE);
            mediaPlayerFragment.setSong(songToPlay);

//            ImageView equalizer = (ImageView) v.findViewById(R.id.tab_title_equalizer_image);
//            if (this.lastSelectedEqualizer != null) {
//                ((AnimationDrawable) this.lastSelectedEqualizer.getBackground()).stop();
//                this.lastSelectedEqualizer.setVisibility(View.GONE);
//            }
//            equalizer.setVisibility(View.VISIBLE);
//            equalizer.setBackgroundResource(R.drawable.ic_equalizer_white_36dp);
//            equalizer.getBackground().setTint(Color.parseColor("#3F51B5"));
//            ((AnimationDrawable) equalizer.getBackground()).start();
//            this.lastSelectedEqualizer = equalizer;
//            onPlayBackButtonClicked();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.album_recycler_view);

        mAdapter = new AlbumArtistAdapter(album.getTrackList());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(AlbumArtistActivity.this, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(AlbumArtistActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public int contentView() {
        return R.layout.activity_album_artist;
    }

    @Override
    public void onPLayLayoutClicked(View v) {
        albumArtistLayout.setVisibility(View.GONE);
        mediaPlayerFragment.setSong(songToPlay);
        mediaPlayerFragment.showMediaPlayerLayout();
    }

    @Override
    public void onBackPressed() {
        albumArtistLayout.setVisibility(View.VISIBLE);
        mediaPlayerFragment.showPlayBackLayout();
    }

    @Override
    public void updateEqualizer(boolean toPlay) {

    }
}
