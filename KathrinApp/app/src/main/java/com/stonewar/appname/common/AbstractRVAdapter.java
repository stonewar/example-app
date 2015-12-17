package com.stonewar.appname.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stonewar.appname.model.Track;

import java.util.List;

/**
 * Created by yandypiedra on 04.12.15.
 */
public abstract class AbstractRVAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    protected IRowViewPagerInteractionListener songCallBack;
    protected int resource;

    public AbstractRVAdapter(int resource, IRowViewPagerInteractionListener songCallBack) {
        this.resource  = resource;
        this.songCallBack = songCallBack;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(resource, parent, false);
        return viewHolder(v);
    }

    @Override
    public abstract void onBindViewHolder(AbstractViewHolder holder, int position);

    public abstract AbstractViewHolder viewHolder(View v);

    @Override
    public abstract int getItemCount();
}
