package com.stonewar.appname.common;

import android.view.View;
import android.widget.ImageView;

import com.stonewar.appname.model.Song;

import java.util.List;

/**
 * Created by yandypiedra on 05.12.15.
 */
public interface IRowViewPagerInteractionListener {

    void selectedView(View v, Song song);
}
