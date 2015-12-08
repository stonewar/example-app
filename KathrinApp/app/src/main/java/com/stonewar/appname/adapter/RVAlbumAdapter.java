package com.stonewar.appname.adapter;

import android.view.View;
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
public class RVAlbumAdapter extends AbstractRVAdapter {

    public RVAlbumAdapter(List<Song> songs, int resource, IRowViewPagerInteractionListener songCallBack) {
        super(songs, resource, songCallBack);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.setSelectedSong(song);
        holder.getImage().setImageBitmap(song.getArtWork());
        ((AlbumViewHolder)holder).title.setText(song.getTitle());
        holder.getAuthor().setText(song.getAuthor());
    }

    @Override
    public AbstractViewHolder viewHolder(View view) {
        return new AlbumViewHolder(view);
    }

    private class AlbumViewHolder extends AbstractViewHolder {

        public TextView title;

        public AlbumViewHolder(View vRow) {
            super(vRow);
            title = (TextView) vRow.findViewById(R.id.tab_album_text_title);
        }

        @Override
        public int imageViewById() {
            return R.id.tab_album_image_song;
        }

        @Override
        public int authorViewById() {
            return R.id.tab_album_text_author;
        }

        @Override
        public void onClick(View v) {
//            Log.d("TAAA", "" + selectedView.getTitle());
            songCallBack.selectedView(v, selectedSong);
        }
    }
}
