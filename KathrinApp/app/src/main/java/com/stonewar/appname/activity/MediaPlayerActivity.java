package com.stonewar.appname.activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.stonewar.appname.R;
import com.stonewar.appname.common.AbstractBaseActivity;
import com.stonewar.appname.model.Track;
import com.stonewar.appname.manager.TrackManager;
import com.stonewar.appname.service.MediaPlayerService;
import com.stonewar.appname.util.AppMediaPlayer;
import com.stonewar.appname.util.Constant;
import com.stonewar.appname.util.Util;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MediaPlayerActivity extends AbstractBaseActivity implements ServiceConnection {

    private static final String TAG = MediaPlayerActivity.class.getName();

    //play_toolbar
    private ImageView imageSong;
    private TextView songTitle, songAuthor;

    //activity_play
    private ImageView songArtwork;
    private SeekBar seekBar;
    private TextView playingTime, songDuration;
    private ImageButton previousButton, playButton, nextButton;

    //Service
    private MediaPlayerService playerService;

    public boolean isServiceBound;

    private int currentSongPosition;
    private int playingTimeInterval;
    private int stoppingTimeInterval;
    private List<Track> selectedSongs;

    private Track songToPlay;
    private Handler playerHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the selected intervals and music
        Intent intent = getIntent();
        selectedSongs = intent.getParcelableArrayListExtra(Constant.SELECTED_SONGS);
        playingTimeInterval = Integer.valueOf(intent.getStringExtra(Constant.PLAYING_TIME_INTERVAL));
        stoppingTimeInterval = Integer.valueOf(intent.getStringExtra(Constant.STOPPING_TIME_INTERVAL));
        songToPlay = selectedSongs.get(currentSongPosition);

        //play_toolbar
        imageSong = (ImageView) findViewById(R.id.tab_title_image_song);
        songTitle = (TextView) findViewById(R.id.tab_title_text_title_song);
        songAuthor = (TextView) findViewById(R.id.tab_title_text_author_song);

        //activity_play
        songArtwork = (ImageView) findViewById(R.id.songArtwork);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        songDuration = (TextView) findViewById(R.id.text_song_duration);
        playingTime = (TextView) findViewById(R.id.text_playing_time);
        previousButton = (ImageButton) findViewById(R.id.previous);
        playButton = (ImageButton) findViewById(R.id.play);
        nextButton = (ImageButton) findViewById(R.id.next);

        Bitmap artwork = TrackManager.findArtworkById(getContentResolver(), getBaseContext(), songToPlay.getId());
        songToPlay.setArtWork(artwork);

        imageSong.setImageBitmap(artwork);
        songTitle.setText(songToPlay.getTitle());
        songAuthor.setText(songToPlay.getAuthor());

        songArtwork.setImageBitmap(artwork);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int mediaPlayerState = playerService.getMediaPlayerState();
                if (mediaPlayerState != AppMediaPlayer.STATE_STARTED) {
                    playerService.play();
                    ((ImageButton) view).setImageBitmap(Util.getBitmap(MediaPlayerActivity.this, R.mipmap.ic_pause_circle_filled_black_48dp));
                } else {
                    ((ImageButton) view).setImageBitmap(Util.getBitmap(MediaPlayerActivity.this, R.mipmap.ic_play_circle_filled_black_48dp));
                    playerService.pause();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerService.next();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerService.previous();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playerService.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Do nothing
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Do nothing
            }
        });

        playerHandler = new Handler() {
            private boolean isSongDurationSet;
            @Override
            public void handleMessage(Message msg) {

                Bundle bundle = msg.getData();

                String action = bundle.getString(Constant.ACTION);
                Log.d(TAG, "Action: " + action);

                if (action.equals(Constant.ACTION_UPDATE_SEEK_BAR)) {
                    double timeElapsed = bundle.getDouble(Constant.TIME_ELAPSED);
                    double finalTime = bundle.getDouble(Constant.SONG_DURATION);
                    if (!isSongDurationSet) {
                        setSongDuration((int) finalTime);
                        isSongDurationSet = true;
                    }
                    setSongProgress((int) timeElapsed);

                } else if (action.equals(Constant.ACTION_SONG_CHANGE)) {
                    setSong((Track) bundle.getParcelable(Constant.PLAYING_SONG));
                    isSongDurationSet = false;
                } else {
                    updatePlaybackButton(action);
                }
            }
        };
    }

    @Override
    public int contentView() {
        return R.layout.activity_play;
    }

    @Override
    public boolean isHomeButtonEnabled() {
        return false;
    }

    @Override
    public boolean isDisplayHomeAsUpEnabled() {
        return false;
    }


    public void setSongDuration(int finalTime) {
        seekBar.setMax(finalTime);
        songDuration.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));
    }

    public void setSongProgress(int timeElapsed) {
        seekBar.setProgress(timeElapsed);
        playingTime.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed),
                TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed)) + 1));
    }

    public void updatePlaybackButton(String action) {
        if (action.equals(Constant.ACTION_SONG_PAUSE)) {
            playButton.setImageBitmap(Util.getBitmap(MediaPlayerActivity.this, R.mipmap.ic_play_circle_filled_black_48dp));
        } else {
            playButton.setImageBitmap(Util.getBitmap(MediaPlayerActivity.this, R.mipmap.ic_pause_circle_filled_black_48dp));
        }
    }

    public void setSong(Track song) {
        songToPlay = song;
        songArtwork.setImageBitmap(songToPlay.getArtWork());
        imageSong.setImageBitmap(songToPlay.getArtWork());
        songTitle.setText(songToPlay.getTitle());
        songAuthor.setText(songToPlay.getAuthor());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to Service
        if (!isServiceBound) {
            Intent serviceIntent = new Intent(this, MediaPlayerService.class);
            bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
            startService(serviceIntent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (isServiceBound) {
            unbindService(this);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MediaPlayerService.PlayerServiceBinder binder = (MediaPlayerService.PlayerServiceBinder) service;
        playerService = binder.getService();
        playerService.setStoppingTimeInterval(stoppingTimeInterval);
        playerService.setPlayingTimeInterval(playingTimeInterval);
        playerService.setSelectedSongs(selectedSongs);
        playerService.setCurrentSong(songToPlay);
        playerService.setCurrentSongPosition(currentSongPosition);
        playerService.setHandler(playerHandler);
        isServiceBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        playerService = null;
        isServiceBound = false;
    }
}
