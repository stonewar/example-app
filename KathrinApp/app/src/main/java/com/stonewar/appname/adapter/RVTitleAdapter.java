package com.stonewar.appname.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stonewar.appname.R;
import com.stonewar.appname.common.AbstractRVAdapter;
import com.stonewar.appname.common.AbstractViewHolder;
import com.stonewar.appname.common.IRowViewPagerInteractionListener;
import com.stonewar.appname.model.Song;

import java.util.List;

/**
 * Created by yandypiedra on 05.12.15.
 */
public class RVTitleAdapter extends AbstractRVAdapter {

    public RVTitleAdapter(List<Song> songs, int resource, IRowViewPagerInteractionListener songCallBack) {
        super(songs, resource, songCallBack);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.setSelectedSong(song);
        holder.getImage().setImageBitmap(song.getArtWork());
        ((TitleViewHolder) holder).title.setText(song.getTitle());
        holder.getAuthor().setText(song.getAuthor());
    }

    @Override
    public AbstractViewHolder viewHolder(View v) {
        return new TitleViewHolder(v);
    }


    private class TitleViewHolder extends AbstractViewHolder {
        public TextView title;
        public ImageView equalizer;

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
            songCallBack.selectedView(v, selectedSong, songs);

        }
    }
}
