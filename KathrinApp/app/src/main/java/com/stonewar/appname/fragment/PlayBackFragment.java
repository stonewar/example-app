package com.stonewar.appname.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.stonewar.appname.R;

public class PlayBackFragment extends Fragment {

      private IPlayBackFragmentInteractionListener playBackButtonListener;
      private ImageView artwork, playback;
      private TextView title, author;

    public interface IPlayBackFragmentInteractionListener {
        void onPlayBackButtonClicked();
        void onViewClicked(View v);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_back, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playBackButtonListener.onViewClicked(v);
            }
        });
        artwork = (ImageView) view.findViewById(R.id.frag_playback_image_song);
        title = (TextView) view.findViewById(R.id.frag_playback_text_title_song);
        author = (TextView) view.findViewById(R.id.frag_playback_text_author_song);
        playback = (ImageView) view.findViewById(R.id.frag_playback_playing_image);
        playback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playBackButtonListener.onPlayBackButtonClicked();
            }
        });
        return view;
    }

    public TextView getTitle() {
        return title;
    }

    public ImageView getArtwork() {
        return artwork;
    }

    public ImageView getPlayback() {
        return playback;
    }

    public TextView getAuthor() {
        return author;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IPlayBackFragmentInteractionListener) {
            playBackButtonListener = (IPlayBackFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IPlayBackFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        playBackButtonListener = null;
    }
}
