package com.stonewar.appname.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.stonewar.appname.R;
import com.stonewar.appname.adapter.SongAdapter;
import com.stonewar.appname.common.AbstractBaseActivity;
import com.stonewar.appname.model.Track;
import com.stonewar.appname.manager.TrackManager;
import com.stonewar.appname.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AbstractBaseActivity {

    private ListView listSongs;
    private List<Track> songs;

    private static final String TAG = MainActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listSongs = (ListView) findViewById(R.id.list_songs);
        new SongsLoader().execute();

    }

    @Override
    public int contentView() {
        return R.layout.activity_main;
    }

    @Override
    public boolean isHomeButtonEnabled() {
        return false;
    }

    @Override
    public boolean isDisplayHomeAsUpEnabled() {
        return false;
    }

    public void clear(View view) {
        for(Track s: songs)
            s.setIsSelected(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((BaseAdapter) listSongs.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    public void accept(View view) {
       List<Track> selectedSongs = new ArrayList<>();
        for(Track s: songs) {
           if(s.isSelected()) {
               //not pretty but otherwise is the size to big!
               s.setArtWork(null);
               selectedSongs.add(s);
           }
        }

        Intent intent = new Intent(this, TimeIntervalActivity.class);
        intent.putParcelableArrayListExtra(Constant.SELECTED_SONGS, (ArrayList<? extends Parcelable>) selectedSongs);
        startActivity(intent);
    }


    private class SongsLoader extends AsyncTask<Void, Void, List<Track>>{

        @Override
        protected List<Track> doInBackground(Void... params) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            return TrackManager.findAllTracks(contentResolver, getBaseContext());
        }

        @Override
        protected void onPostExecute(List<Track> foundSongs) {
            songs = foundSongs;
            listSongs.setAdapter(new SongAdapter(getBaseContext(), songs));
        }
    }


}
