package com.stonewar.appname.fragment;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.stonewar.appname.R;
import com.stonewar.appname.adapter.RVAlbumAdapter;
import com.stonewar.appname.adapter.RVArtistAdapter;
import com.stonewar.appname.common.AbstractViewPagerFragment;
import com.stonewar.appname.manager.SongManager;
import com.stonewar.appname.model.Song;

import java.util.List;

/**
 * Created by yandypiedra on 05.12.15.
 */
public class AlbumFragment extends AbstractViewPagerFragment {

    private static final String TAG = ArtistFragment.class.getName();

    @Override
    public void executeThread() {
        new AlbumLoader().execute();
    }

    @Override
    public CharSequence getTitle() {
        //TODO put it in the resources for internationalization
        return "Album";
    }

    @Override
    public void selectedSong(Song song) {
        Log.d(TAG, "Selected Song :" + song.getId() + ", " + song.getAuthor() + ", " + song.getTitle());
    }

    private class AlbumLoader extends AsyncTask<Void, Void, List<Song>> {

        @Override
        protected List<Song> doInBackground(Void... params) {
            ContentResolver contentResolver = getActivity().getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            return SongManager.findAllSongGroupedByArtists(contentResolver, uri);
        }

        @Override
        protected void onPostExecute(List<Song> foundSongs) {
            songs = foundSongs;
            rvAdapter = new RVAlbumAdapter(songs, R.layout.tab_album_row, AlbumFragment.this);
            recyclerView.setAdapter(rvAdapter);
            recyclerView.setHasFixedSize(true);
            layoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}
