package com.stonewar.appname.common;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stonewar.appname.R;
import com.stonewar.appname.model.Song;

import java.util.List;

/**
 * Created by yandypiedra on 05.12.15.
 */
public abstract class AbstractViewPagerFragment extends Fragment implements IRowViewPagerInteractionListener {

    protected RecyclerView recyclerView;
    protected AbstractRVAdapter rvAdapter;
    protected List<Song> songs;
    protected RecyclerView.LayoutManager layoutManager;
    protected IRowViewPagerInteractionListener iRowViewPagerCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IRowViewPagerInteractionListener) {
            iRowViewPagerCallBack = (IRowViewPagerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IRowViewPagerInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iRowViewPagerCallBack = null;
    }

    @Override
    public void selectedView(View v, Song song){
        iRowViewPagerCallBack.selectedView(v, song);
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
