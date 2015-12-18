package com.stonewar.appname.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.stonewar.appname.R;
import com.stonewar.appname.adapter.AlbumArtistAdapter;
import com.stonewar.appname.googlecode.DividerItemDecoration;
import com.stonewar.appname.manager.TrackManager;
import com.stonewar.appname.model.Album;
import com.stonewar.appname.util.Constant;

public class AlbumArtistActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_artist);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Album album = getIntent().getParcelableExtra(Constant.ALBUM);

        ImageView artistOrArtworkImg = (ImageView) findViewById(R.id.album_or_author_image);

        Bitmap artistOrArtwork = TrackManager.findArtworkById(getContentResolver(), this, album.getTrackList().get(0).getId());

        artistOrArtworkImg.setImageBitmap(artistOrArtwork);

        mRecyclerView = (RecyclerView) findViewById(R.id.album_recycler_view);

        mAdapter = new AlbumArtistAdapter(album.getTrackList());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(AlbumArtistActivity.this, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(AlbumArtistActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        new TracksLoader().execute();
    }

//    private class TracksLoader extends AsyncTask<Void, Void, List<Track>> {
//
//        @Override
//        protected List<Track> doInBackground(Void... params) {
//            ContentResolver contentResolver = getContentResolver();
//            return TrackManager.findAllTracks(contentResolver, getBaseContext());
//        }
//
//        @Override
//        protected void onPostExecute(List<Track> foundSongs) {
//            mAdapter = new AlbumArtistAdapter(foundSongs);
//            mRecyclerView.setAdapter(mAdapter);
//            mRecyclerView.setHasFixedSize(true);
//            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(AlbumArtistActivity.this, DividerItemDecoration.VERTICAL_LIST);
//            mRecyclerView.addItemDecoration(itemDecoration);
//            // use a linear layout manager
//            mLayoutManager = new LinearLayoutManager(AlbumArtistActivity.this);
//            mRecyclerView.setLayoutManager(mLayoutManager);
//        }
//    }
}
