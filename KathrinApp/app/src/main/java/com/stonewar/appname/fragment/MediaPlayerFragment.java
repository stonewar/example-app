package com.stonewar.appname.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.stonewar.appname.R;
import com.stonewar.appname.model.Song;
import com.stonewar.appname.util.Constant;
import com.stonewar.appname.util.Util;

import java.util.concurrent.TimeUnit;

public class MediaPlayerFragment extends Fragment {


    private static final String TAG = MediaPlayerFragment.class.getName();

    public interface IMediaPlayerFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private IMediaPlayerFragmentInteractionListener mListener;

    //play_toolbar
    private ImageView imageSong;
    private TextView songTitle, songAuthor;

    //activity_play
    private ImageView songArtwork;
    private SeekBar seekBar;
    private TextView playingTime, songDuration;
    private ImageButton previousButton, playButton, nextButton;

    public static MediaPlayerFragment newInstance(String param1, String param2) {
        MediaPlayerFragment fragment = new MediaPlayerFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_player, container, false);

        //play_toolbar
        imageSong = (ImageView) view.findViewById(R.id.tab_title_image_song);
        songTitle = (TextView) view.findViewById(R.id.tab_title_text_title_song);
        songAuthor = (TextView) view.findViewById(R.id.tab_title_text_author_song);

        //activity_play
        songArtwork = (ImageView) view.findViewById(R.id.songArtwork);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        songDuration = (TextView) view.findViewById(R.id.text_song_duration);
        playingTime = (TextView) view.findViewById(R.id.text_playing_time);
        previousButton = (ImageButton) view.findViewById(R.id.previous);
        playButton = (ImageButton) view.findViewById(R.id.play);
        nextButton = (ImageButton) view.findViewById(R.id.next);

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

    public void setSong(Song song) {
        songArtwork.setImageBitmap(song.getArtWork());
        imageSong.setImageBitmap(song.getArtWork());
        songTitle.setText(song.getTitle());
        songAuthor.setText(song.getAuthor());
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
