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
import com.stonewar.appname.model.Song;
import com.stonewar.appname.manager.SongManager;
import com.stonewar.appname.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AbstractBaseActivity {

    private ListView listSongs;
    private List<Song> songs;

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
        for(Song s: songs)
            s.setIsSelected(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((BaseAdapter) listSongs.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    public void accept(View view) {
       List<Song> selectedSongs = new ArrayList<>();
        for(Song s: songs) {
           if(s.isSelected()) {
               //not pretty but otherwise is the size to big!
               s.setArtWork(null);
               selectedSongs.add(s);
           }
        }

        Intent intent = new Intent(this, TimeIntervalActivityAbstract.class);
        intent.putParcelableArrayListExtra(Constant.SELECTED_SONGS, (ArrayList<? extends Parcelable>) selectedSongs);
        startActivity(intent);
    }


    private class SongsLoader extends AsyncTask<Void, Void, List<Song>>{

        @Override
        protected List<Song> doInBackground(Void... params) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            return SongManager.findAllSongs(contentResolver, uri);
        }

        @Override
        protected void onPostExecute(List<Song> foundSongs) {
            songs = foundSongs;
            listSongs.setAdapter(new SongAdapter(getBaseContext(), songs));
        }
    }


}
