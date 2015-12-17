package com.stonewar.appname.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stonewar.appname.R;
import com.stonewar.appname.common.AbstractRVAdapter;
import com.stonewar.appname.common.AbstractViewHolder;
import com.stonewar.appname.common.IRowViewPagerInteractionListener;
import com.stonewar.appname.model.Track;

import java.util.List;

/**
 * Created by yandypiedra on 05.12.15.
 */
public class RVTitleAdapter extends AbstractRVAdapter {

    private List<Track> tracks;

    public RVTitleAdapter(List<Track> tracks, int resource, IRowViewPagerInteractionListener songCallBack) {
        super(resource, songCallBack);
        this.tracks = tracks;
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        Track track = tracks.get(position);
        TitleViewHolder titleViewHolder = (TitleViewHolder)holder;
        titleViewHolder.selectTrack = track;
        titleViewHolder.getImage().setImageBitmap(track.getArtWork());
        titleViewHolder.title.setText(track.getTitle());
        titleViewHolder.getAuthor().setText(track.getAuthor());
    }

    @Override
    public AbstractViewHolder viewHolder(View v) {
        return new TitleViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }


    private class TitleViewHolder extends AbstractViewHolder {
        public TextView title;
        public ImageView equalizer;
        public Track selectTrack;

        public TitleViewHolder(View vRow) {
            super(vRow);
            title = (TextView) vRow.findViewById(R.id.tab_title_text_title_song);
            equalizer = (ImageView) vRow.findViewById(R.id.tab_title_equalizer_image);
        }

        @Override
        public int imageViewById() {
            return R.id.tab_title_image_song;
        }

        @Override
        public int authorViewById() {
            return R.id.tab_title_text_author_song;
        }

        @Override
        public void onClick(View v) {
//            if (lastSelectedEqualizer != null) {
//                ((AnimationDrawable) lastSelectedEqualizer.getBackground()).stop();
//                lastSelectedEqualizer.setVisibility(View.GONE);
//            }
//            equalizer.setVisibility(View.VISIBLE);
//            equalizer.setBackgroundResource(R.drawable.ic_equalizer_white_36dp);
//            equalizer.getBackground().setTint(Color.parseColor("#3F51B5"));
//            ((AnimationDrawable) equalizer.getBackground()).start();
//            lastSelectedEqualizer = equalizer;
            songCallBack.selectedView(v, selectTrack, tracks, IRowViewPagerInteractionListener.ViewPagerAction.Title);

        }
    }
}
