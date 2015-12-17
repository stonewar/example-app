package com.stonewar.appname.common;

import android.view.View;

import com.stonewar.appname.model.Track;

import java.util.List;

/**
 * Created by yandypiedra on 05.12.15.
 */
public interface IRowViewPagerInteractionListener {

    enum ViewPagerAction {Album, Artist, Title} //TODO change to tracks

    void selectedView(View v, Track song, List<Track> songs, ViewPagerAction action);
}
