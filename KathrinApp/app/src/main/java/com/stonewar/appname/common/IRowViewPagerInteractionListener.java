package com.stonewar.appname.common;

import android.view.View;

import com.stonewar.appname.model.Album;
import com.stonewar.appname.model.Track;

import java.util.List;

/**
 * Created by yandypiedra on 05.12.15.
 */
public interface IRowViewPagerInteractionListener {

    void selectedTrack(View v, Track song, List<Track> songs);

    void selectedAlbum(View v, Album album);

    //TODO analise this method
    void selectedArtist(View v, List<Track> songs);
}
