package com.stonewar.appname.fragment;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.stonewar.appname.R;
import com.stonewar.appname.adapter.RVTitleAdapter;
import com.stonewar.appname.common.AbstractViewPagerFragment;
import com.stonewar.appname.googlecode.DividerItemDecoration;
import com.stonewar.appname.manager.SongManager;
import com.stonewar.appname.model.Song;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by yandypiedra on 04.12.15.
 */
public class TitleFragment extends AbstractViewPagerFragment {

    private static final String TAG = TitleFragment.class.getName();

    @Override
    public void executeThread() {
        new SongsLoader().execute();
    }

    @Override
    public CharSequence getTitle() {
        //TODO
        return "Title";
    }

    private class SongsLoader extends AsyncTask<Void, Void, List<Song>> {

        @Override
        protected List<Song> doInBackground(Void... params) {
            ContentResolver contentResolver = getActivity().getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            return SongManager.findAllSongs(contentResolver, uri);
        }

        @Override
        protected void onPostExecute(List<Song> foundSongs) {
            songs = foundSongs;
            rvAdapter = new RVTitleAdapter(songs, R.layout.tab_title_row, TitleFragment.this);
            recyclerView.setAdapter(rvAdapter);
            recyclerView.setHasFixedSize(true);
            RecyclerView.ItemDecoration itemDecoration = new
                    DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
            recyclerView.addItemDecoration(itemDecoration);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}
