package com.stonewar.appname.fragment;

import android.content.ContentResolver;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.stonewar.appname.R;
import com.stonewar.appname.adapter.RVAlbumAdapter;
import com.stonewar.appname.common.AbstractViewPagerFragment;
import com.stonewar.appname.manager.AlbumManager;
import com.stonewar.appname.model.Album;

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

    private class AlbumLoader extends AsyncTask<Void, Void, List<Album>> {

        @Override
        protected List<Album> doInBackground(Void... params) {
            ContentResolver contentResolver = getActivity().getContentResolver();
            final String selection = "1 = 1) GROUP BY (" + AlbumManager.ALBUM_NAME;
            return AlbumManager.findAllAlbums(getContext(), contentResolver, selection);
        }

        @Override
        protected void onPostExecute(List<Album> foundAlbums) {
            Log.d(TAG, "Found albums :" + foundAlbums.size());
            rvAdapter = new RVAlbumAdapter(foundAlbums, R.layout.tab_album_row, AlbumFragment.this);
            recyclerView.setAdapter(rvAdapter);
            recyclerView.setHasFixedSize(true);
            layoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}
