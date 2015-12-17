package com.stonewar.appname.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.NumberPicker;

import com.stonewar.appname.common.AbstractViewPagerFragment;
import com.stonewar.appname.fragment.AlbumFragment;
import com.stonewar.appname.fragment.ArtistFragment;
import com.stonewar.appname.fragment.TitleFragment;

/**
 * Created by yandypiedra on 13.11.15.
 */
public class Factory {

    public static NumberPicker.Formatter createNumberPickerFormatter(final int formattIncretmentValue){
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int temp = value * formattIncretmentValue;
                return "" + temp;
            }
        };

        return formatter;
    }

    public static PendingIntent createPlayBackPendingIntent(Context context, int which) {
        PendingIntent pendingIntent = null;
        Intent action;
        switch (which) {
            case 1:
                // Play and pause
                action = new Intent(Constant.ACTION_PLAY_PAUSE_SONG);
                pendingIntent = PendingIntent.getBroadcast(context, 1, action, 0);
                return pendingIntent;
            case 2:
                // Next song
                action = new Intent(Constant.ACTION_NEXT_SONG);
                pendingIntent = PendingIntent.getBroadcast(context, 2, action, 0);
                return pendingIntent;
            case 3:
                // Previous song
                action = new Intent(Constant.ACTION_PREVIOUS_SONG);
                pendingIntent = PendingIntent.getBroadcast(context, 3, action, 0);
                return pendingIntent;
            default:
                break;
        }
        return pendingIntent;
    }

    public static AbstractViewPagerFragment createViewPagerFragment(int fragmentPosition){
        AbstractViewPagerFragment fragment = null;
        switch (fragmentPosition){
            case 1 :
                fragment = new ArtistFragment();
                break;
            case 2 :
                fragment = new AlbumFragment();
                break;
            case 3:
                fragment = new TitleFragment();
            default:
                break;
        }
        return fragment;
    }
}
