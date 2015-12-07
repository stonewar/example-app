package com.stonewar.appname.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stonewar.appname.R;
import com.stonewar.appname.model.Song;
import com.stonewar.appname.util.Constant;

import java.util.List;

/**
 * Created by yandypiedra on 05.12.15.
 */
public abstract class AbstractViewPagerFragment extends Fragment implements ISongCallBack {

    protected RecyclerView recyclerView;
    protected AbstractRVAdapter rvAdapter;
    protected List<Song> songs;
    protected RecyclerView.LayoutManager layoutManager;
    protected ISongCallBack iSongCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ISongCallBack) {
            iSongCallBack = (ISongCallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ISongCallBack");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iSongCallBack = null;
    }

    @Override
    public void selectedSong(Song song) {
        iSongCallBack.selectedSong(song);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_view_pager, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        executeThread();
        return v;
    }

    public abstract void executeThread();

    public abstract CharSequence getTitle();
}
