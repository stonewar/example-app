package com.stonewar.appname.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.*;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.stonewar.appname.R;
import com.stonewar.appname.activity.MainActivity;
import com.stonewar.appname.broadcast.MediaPlayerBroadCastReceiver;
import com.stonewar.appname.common.IMediaPlayerController;
import com.stonewar.appname.manager.TrackManager;
import com.stonewar.appname.model.Track;
import com.stonewar.appname.util.AppMediaPlayer;
import com.stonewar.appname.util.Constant;
import com.stonewar.appname.util.Factory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class MediaPlayerService extends Service implements IMediaPlayerController,
        OnCompletionListener, OnErrorListener, OnPreparedListener {

    private static final String TAG = MediaPlayerService.class.getName();

    public static final int NOTIFY_ID = 101;

    private AppMediaPlayer mediaPlayer;
    private ScheduledExecutorService scheduleTaskExecutor;
    private int playingTimeInterval;
    private int stoppingTimeInterval;
    private List<Track> selectedSongs;
    private int currentSongPosition;
    private Track currentSong;
    private Future<?> futureStartPause, futureCurrentPositionDuration;

    private double songDuration;
    private double timeElapsed;

    private BroadcastReceiver mediaPlayerReceiver;
    private Handler playerHandler;

    // Binder given to clients
    private final IBinder mBinder = new PlayerServiceBinder();

    public class PlayerServiceBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new AppMediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        mediaPlayerReceiver = new MediaPlayerBroadCastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_NEXT_SONG);
        intentFilter.addAction(Constant.ACTION_PREVIOUS_SONG);
        intentFilter.addAction(Constant.ACTION_PLAY_PAUSE_SONG);
        intentFilter.addAction(Constant.ACTION_STOP_SERVICE);
        registerReceiver(mediaPlayerReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destroyed");
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        unregisterReceiver(mediaPlayerReceiver);
        scheduleTaskExecutor.shutdown();
        mediaPlayer.release();
        mediaPlayer = null;
        playerHandler = null;
        stopForeground(true);
    }

    @Override
    public void play() {
        release();
        Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSong.getId());
        try {
            if (mediaPlayer.getCurrentState() != AppMediaPlayer.STATE_PAUSED) {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(this, uri);
                mediaPlayer.prepare();
            }
            schedulePlayingPauseInterval();
            scheduleDurationCurrentPosition();

        } catch (IOException e) {
            e.printStackTrace();
        }
        startForeground(NOTIFY_ID, createNotification());
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
        release();
        //TODO
        stopForeground(true);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIFY_ID, createNotification());
    }

    @Override
    public void next() {
        currentSongPosition = (currentSongPosition + 1) % selectedSongs.size();
        currentSong = selectedSongs.get(currentSongPosition);
        Bitmap artwork = TrackManager.findArtworkById(getContentResolver(), MediaPlayerService.this, currentSong.getId());
        currentSong.setArtWork(artwork);
        handleSongChanged();
        mediaPlayer.stop();
        play();
    }

    @Override
    public void previous() {
        if (currentSongPosition == 0) {
            currentSongPosition = selectedSongs.size() - 1;
        } else {
            currentSongPosition = (currentSongPosition - 1) % selectedSongs.size();
        }
        currentSong = selectedSongs.get(currentSongPosition);
        Bitmap artwork = TrackManager.findArtworkById(getContentResolver(), MediaPlayerService.this, currentSong.getId());
        currentSong.setArtWork(artwork);
        handleSongChanged();
        mediaPlayer.stop();
        play();
    }

    @Override
    public void stop(){
        mediaPlayer.stop();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    private void schedulePlayingPauseInterval() {
        futureStartPause = scheduleTaskExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Start playing");
                try {
                    Bundle data = null;
                    mediaPlayer.start();
                    data = new Bundle();
                    data.putString(Constant.ACTION, Constant.ACTION_SONG_STARTED);
                    Message message = playerHandler.obtainMessage();
                    message.setData(data);
                    message.sendToTarget();
                    Thread.sleep(playingTimeInterval * 1000);
                    Log.i(TAG, "Stopping");
                    mediaPlayer.pause();
                    data = new Bundle();
                    data.putString(Constant.ACTION, Constant.ACTION_SONG_PAUSE);
                    Message messageStop = playerHandler.obtainMessage();
                    messageStop.setData(data);
                    messageStop.sendToTarget();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, stoppingTimeInterval, TimeUnit.SECONDS);
    }

    private void scheduleDurationCurrentPosition() {
        futureCurrentPositionDuration = scheduleTaskExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    timeElapsed = mediaPlayer.getCurrentPosition();
                    songDuration = mediaPlayer.getDuration();
                    Bundle data = new Bundle();
                    data.putString(Constant.ACTION, Constant.ACTION_UPDATE_SEEK_BAR);
                    data.putDouble(Constant.TIME_ELAPSED, timeElapsed);
                    data.putDouble(Constant.SONG_DURATION, songDuration);
                    Message message = playerHandler.obtainMessage();
                    message.setData(data);
                    message.sendToTarget();
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    private void handleSongChanged() {
        Bundle data = new Bundle();
        data.putString(Constant.ACTION, Constant.ACTION_SONG_CHANGE);
        data.putParcelable(Constant.PLAYING_SONG, currentSong);
        Message message = playerHandler.obtainMessage();
        message.setData(data);
        message.sendToTarget();
    }

    private void release() {
        if (futureCurrentPositionDuration != null) {
            futureCurrentPositionDuration.cancel(true);
        }
        if (futureStartPause != null) {
            futureStartPause.cancel(true);
        }
    }

    private Notification createNotification() {

        Intent intent = new Intent(Constant.ACTION_STOP_SERVICE);
        PendingIntent stopServicePendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        int playPauseIcon = R.mipmap.uamp_ic_play_arrow_white_24dp;
        if(mediaPlayer.isPlaying()){
            playPauseIcon = R.mipmap.uamp_ic_pause_white_24dp;
        }

        // Create a new MediaSession
        final MediaSession mediaSession = new MediaSession(this, "debug tag");
        // Indicate you're ready to receive media commands
//          mediaSession.setActive(true);
        // Attach a new Callback to receive MediaSession updates
//            mediaSession.setCallback(new MediaSession.Callback() {
//            @Override
//            public void onPlay() {
//                super.onPlay();
//                Log.d("Play", "Play");
//            }
//
//            @Override
//            public void onPause() {
//                super.onPause();
//                Log.d("Pause", "Pause");
//            }

        // Implement your callbacks

//            });
        // Indicate you want to receive transport controls via your Callback
//            mediaSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Create a new Notification
        Notification notification = new Notification.Builder(this)
                .setContentIntent(getNotificationContentIntent())
                .setDeleteIntent(stopServicePendingIntent)
                        // Hide the timestamp
                .setShowWhen(false)
                        // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                        // Set the Notification style
                .setStyle(new Notification.MediaStyle()
                                // Attach our MediaSession token
                                .setMediaSession(mediaSession.getSessionToken())
                                        // Show our playback controls in the compat vie
                                .setShowActionsInCompactView(0, 1, 2)
                )
                        // Set the Notification color
                .setColor(0xFFDB4437)
                        // Set the large and small icons
                .setLargeIcon(currentSong.getArtWork())
                .setSmallIcon(R.mipmap.ic_launcher)
                        // Set Notification content information
                .setContentText(currentSong.getAuthor())
//                    .setContentInfo(currentSong.getAuthor())
                .setContentTitle(currentSong.getTitle())
                        // Add some playback controls
                .addAction(R.mipmap.ic_skip_previous_white_24dp, "prev", Factory.createPlayBackPendingIntent(this, 3))
                .addAction(playPauseIcon, "pause_play", Factory.createPlayBackPendingIntent(this, 1))
                .addAction(R.mipmap.ic_skip_next_white_24dp, "next", Factory.createPlayBackPendingIntent(this, 2))
                .build();

        return notification;
    }

    private PendingIntent getNotificationContentIntent() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this, 0, mainActivity, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // mp.reset();
        mediaPlayer.setCurrentState(AppMediaPlayer.STATE_ERROR);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.setCurrentState(AppMediaPlayer.STATE_PREPARED);
        mp.start();
    }

    public void setPlayingTimeInterval(int playingTimeInterval) {
        this.playingTimeInterval = playingTimeInterval;
    }

    public void setStoppingTimeInterval(int stoppingTimeInterval) {
        this.stoppingTimeInterval = stoppingTimeInterval;
    }

    public void setSelectedSongs(List<Track> selectedSongs) {
        this.selectedSongs = selectedSongs;
    }

    public void setCurrentSongPosition(int position) {
        this.currentSongPosition = position;
    }

    public void setCurrentSong(Track currentSong) {
        this.currentSong = currentSong;
    }

    public int getMediaPlayerState() {
        return mediaPlayer.getCurrentState();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void setHandler(Handler handler) {
        this.playerHandler = handler;
    }
}
