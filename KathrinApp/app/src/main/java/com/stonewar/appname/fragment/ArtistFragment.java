package com.stonewar.appname.fragment;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;

import com.stonewar.appname.R;
import com.stonewar.appname.adapter.RVArtistAdapter;
import com.stonewar.appname.common.AbstractViewPagerFragment;
import com.stonewar.appname.manager.TrackManager;
import com.stonewar.appname.model.Track;

import java.util.List;

/**
 * Created by yandypiedra on 04.12.15.
 */
public class ArtistFragment extends AbstractViewPagerFragment {

    private static final String TAG = ArtistFragment.class.getName();
    private List<Track> songs;

    @Override
    public void executeThread() {
          new ArtistLoader().execute();
    }

    @Override
    public CharSequence getTitle() {
        //TODO put it in the resources for internationalization
        return "Artist";
    }

    private class ArtistLoader extends AsyncTask<Void, Void, List<Track>> {

        @Override
        protected List<Track> doInBackground(Void... params) {
            ContentResolver contentResolver = getActivity().getContentResolver();
            return TrackManager.findAllTracksGroupedBy(contentResolver, getContext(), MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST);
        }

        @Override
        protected void onPostExecute(List<Track> foundSongs) {
            songs = foundSongs;
            rvAdapter = new RVArtistAdapter(songs, R.layout.tab_artist_row, ArtistFragment.this);
            recyclerView.setAdapter(rvAdapter);
            recyclerView.setHasFixedSize(true);
            layoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}
