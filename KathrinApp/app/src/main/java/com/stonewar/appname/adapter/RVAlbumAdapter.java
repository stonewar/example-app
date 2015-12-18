package com.stonewar.appname.adapter;

import android.view.View;
import android.widget.TextView;

import com.stonewar.appname.R;
import com.stonewar.appname.common.AbstractRVAdapter;
import com.stonewar.appname.common.AbstractViewHolder;
import com.stonewar.appname.common.IRowViewPagerInteractionListener;
import com.stonewar.appname.model.Album;
import com.stonewar.appname.model.Track;

import java.util.List;

/**
 * Created by yandypiedra on 05.12.15.
 */
public class RVAlbumAdapter extends AbstractRVAdapter {

    private List<Album> albums;

    public RVAlbumAdapter(List<Album> albums, int resource, IRowViewPagerInteractionListener songCallBack) {
        super(resource, songCallBack);
        this.albums = albums;
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        Album album = albums.get(position);
        AlbumViewHolder albumViewHolder = (AlbumViewHolder)holder;
        albumViewHolder.selectedAlbum = album;
        List<Track> trackList = album.getTrackList();
        if(trackList != null && trackList.size() > 0){
                holder.getImage().setImageBitmap(trackList.get(0).getArtWork());
        }
        albumViewHolder.title.setText(album.getTitle());
        holder.getAuthor().setText(album.getArtist());
    }

    @Override
    public AbstractViewHolder viewHolder(View view) {
        return new AlbumViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    private class AlbumViewHolder extends AbstractViewHolder {

        public TextView title;
        public Album selectedAlbum;

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
            songCallBack.selectedAlbum(v, selectedAlbum);
        }
    }
}
