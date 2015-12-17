package com.stonewar.appname.fragment;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stonewar.appname.R;
import com.stonewar.appname.adapter.RVTitleAdapter;
import com.stonewar.appname.common.AbstractViewPagerFragment;
import com.stonewar.appname.googlecode.DividerItemDecoration;
import com.stonewar.appname.manager.TrackManager;
import com.stonewar.appname.model.Track;

import java.util.List;

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

    private class SongsLoader extends AsyncTask<Void, Void, List<Track>> {

        @Override
        protected List<Track> doInBackground(Void... params) {
            ContentResolver contentResolver = getActivity().getContentResolver();
            return TrackManager.findAllTracks(contentResolver, getContext());
        }

        @Override
        protected void onPostExecute(List<Track> foundSongs) {
            rvAdapter = new RVTitleAdapter(foundSongs, R.layout.tab_title_row, TitleFragment.this);
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
