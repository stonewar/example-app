package com.stonewar.appname.adapter;

import android.util.Log;
import android.view.View;

import com.stonewar.appname.R;
import com.stonewar.appname.common.AbstractRVAdapter;
import com.stonewar.appname.common.AbstractViewHolder;
import com.stonewar.appname.common.ISongCallBack;
import com.stonewar.appname.model.Song;

import java.util.List;

/**
 * Created by yandypiedra on 05.12.15.
 */
public class RVArtistAdapter extends AbstractRVAdapter {

    public RVArtistAdapter(List<Song> songs, int resource, ISongCallBack songCallBack) {
        super(songs, resource, songCallBack);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.setSelectedSong(song);
        holder.getImage().setImageBitmap(song.getArtWork());
        holder.getAuthor().setText(song.getAuthor());
    }

    @Override
    public AbstractViewHolder viewHolder(View view) {
        return new ArtistViewHolder(view);
    }

    private class ArtistViewHolder extends AbstractViewHolder{

        public ArtistViewHolder(View vRow) {
            super(vRow);
        }

        @Override
        public int imageViewById() {
            return R.id.tab_artist_image_song;
        }

        @Override
        public int authorViewById() {
            return R.id.tab_artist_text_author;
        }

        @Override
        public void onClick(View v) {
            Log.d("TAAA", ""+selectedSong.getTitle());
        }
    }

}
