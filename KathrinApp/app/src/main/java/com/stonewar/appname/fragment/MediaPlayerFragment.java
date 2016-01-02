package com.stonewar.appname.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.stonewar.appname.R;
import com.stonewar.appname.model.Track;
import com.stonewar.appname.util.Constant;
import com.stonewar.appname.util.Util;

import java.util.concurrent.TimeUnit;

public class MediaPlayerFragment extends Fragment {
    
    private static final String TAG = MediaPlayerFragment.class.getName();

    public interface IMediaPlayerFragmentInteractionListener {
        void play();
        void next();
        void previous();
        void seekTo(int progress);
        void onPLayLayoutClicked(View v);
        void onPlayBackButtonClicked();
    }

    private IMediaPlayerFragmentInteractionListener mListener;

    private RelativeLayout mediaPlayerLayout;

    //play_toolbar
    private ImageView imageSong;
    private TextView songTitle, songAuthor;

    //activity_play
    private ImageView songArtwork;
    private SeekBar seekBar;
    private TextView playingTime, songDuration;
    private ImageButton previousButton, playButton, nextButton;

    private Track songToPlay;

    private ImageView artwork, playback;
    private TextView title, author;
    private RelativeLayout playBackLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
             songToPlay = arguments.getParcelable(Constant.PLAYING_SONG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_player, container, false);

        //play_back layout
        playBackLayout = (RelativeLayout) view.findViewById(R.id.play_back_layout);
        playBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPLayLayoutClicked(v);
            }
        });
        artwork = (ImageView) view.findViewById(R.id.frag_playback_image_song);
        title = (TextView) view.findViewById(R.id.frag_playback_text_title_song);
        author = (TextView) view.findViewById(R.id.frag_playback_text_author_song);
        playback = (ImageView) view.findViewById(R.id.frag_playback_playing_image);

        mediaPlayerLayout = (RelativeLayout) view.findViewById(R.id.media_player_layout);

        //play_toolbar
        imageSong = (ImageView) view.findViewById(R.id.tab_title_image_song);
        songTitle = (TextView) view.findViewById(R.id.title_track);
        songAuthor = (TextView) view.findViewById(R.id.album_or_author_track);

        //activity_play
        songArtwork = (ImageView) view.findViewById(R.id.songArtwork);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        songDuration = (TextView) view.findViewById(R.id.text_song_duration);
        playingTime = (TextView) view.findViewById(R.id.text_playing_time);
        previousButton = (ImageButton) view.findViewById(R.id.previous);
        playButton = (ImageButton) view.findViewById(R.id.play);
        nextButton = (ImageButton) view.findViewById(R.id.next);

        artwork = (ImageView) view.findViewById(R.id.frag_playback_image_song);
        title = (TextView) view.findViewById(R.id.frag_playback_text_title_song);
        author = (TextView) view.findViewById(R.id.frag_playback_text_author_song);
        playback = (ImageView) view.findViewById(R.id.frag_playback_playing_image);
        playback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPlayBackButtonClicked();
            }
        });

        playButton.setImageBitmap(Util.getBitmap(getActivity(), R.mipmap.ic_pause_circle_filled_black_48dp));

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.play();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.next();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.previous();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mListener.seekTo(seekBar.getProgress());
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

        return view;
    }


    public void setSongDuration(int finalTime) {
        seekBar.setMax(finalTime);
        songDuration.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));
    }

    public void setSongProgress(int timeElapsed) {
        Log.d(TAG, "Updating progress bar: "+timeElapsed);
        seekBar.setProgress(timeElapsed);
        playingTime.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed),
                TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed)) + 1));
    }

    public void updatePlaybackButton(String action) {
        if (action.equals(Constant.ACTION_SONG_PAUSE)) {
            playButton.setImageBitmap(Util.getBitmap(getActivity(), R.mipmap.ic_play_circle_filled_black_48dp));
        } else {
            playButton.setImageBitmap(Util.getBitmap(getActivity(), R.mipmap.ic_pause_circle_filled_black_48dp));
        }
    }

    public void setSong(Track song) {
        songToPlay = song;
        songArtwork.setImageBitmap(songToPlay.getArtWork());
        imageSong.setImageBitmap(songToPlay.getArtWork());
        songTitle.setText(songToPlay.getTitle());
        songAuthor.setText(songToPlay.getAuthor());

        artwork.setImageBitmap(songToPlay.getArtWork());
        title.setText(song.getTitle());
        author.setText(song.getAuthor());

    }

    public void showMediaPlayerLayout(){
        playBackLayout.setVisibility(View.GONE);
        mediaPlayerLayout.setVisibility(View.VISIBLE);
    }

//    final ViewPager pager,
//    final SlidingTabLayout tabs
    public void showPlayBackLayout(){

//        Animation outAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
//
//        mediaPlayerLayout.setAnimation(outAnimation);
//        outAnimation.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationEnd(Animation arg0) {
//                mediaPlayerLayout.setVisibility(View.GONE);
//                playBackLayout.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                //
//            }
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//                pager.setVisibility(View.VISIBLE);
//                tabs.setVisibility(View.VISIBLE);
//            }
//        });
//        mediaPlayerLayout.startAnimation(outAnimation);

        playBackLayout.setVisibility(View.VISIBLE);
        mediaPlayerLayout.setVisibility(View.GONE);
    }

    public ImageView getPlayback() {
        return playback;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IMediaPlayerFragmentInteractionListener) {
            mListener = (IMediaPlayerFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IMediaPlayerFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
