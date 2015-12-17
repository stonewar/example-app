package com.stonewar.appname.broadcast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.stonewar.appname.common.IMediaPlayerController;
import com.stonewar.appname.util.Constant;

/**
 * Created by yandypiedra on 29.11.15.
 */
public class MediaPlayerBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = MediaPlayerBroadCastReceiver.class.getName();
    private IMediaPlayerController mediaPlayerController;

    public MediaPlayerBroadCastReceiver(IMediaPlayerController mediaPlayerController) {
        this.mediaPlayerController = mediaPlayerController;
    }

    @Override
    public void onReceive(Context context, final Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "Action: " + action);

        if (action.equals(Constant.ACTION_NEXT_SONG)) {
            mediaPlayerController.next();
        } else if (action.equals(Constant.ACTION_PREVIOUS_SONG)) {
            mediaPlayerController.previous();
        }
        else if(action.equals(Constant.ACTION_STOP_SERVICE)){
            ((Service)mediaPlayerController).stopSelf();
        }
        else {
            if (mediaPlayerController.isPlaying()) {
                mediaPlayerController.pause();
            } else {
                mediaPlayerController.play();
            }
        }
    }
}
