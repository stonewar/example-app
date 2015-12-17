package com.stonewar.appname.adapter;

import android.view.View;

import com.stonewar.appname.R;
import com.stonewar.appname.common.AbstractRVAdapter;
import com.stonewar.appname.common.AbstractViewHolder;
import com.stonewar.appname.common.IRowViewPagerInteractionListener;
import com.stonewar.appname.model.Track;

import java.util.List;

/**
 * Created by yandypiedra on 05.12.15.
 */
public class RVArtistAdapter extends AbstractRVAdapter {

    private List<Track> tracks;

    public RVArtistAdapter(List<Track> tracks, int resource, IRowViewPagerInteractionListener songCallBack) {
        super(resource, songCallBack);
        this.tracks = tracks;
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        Track track = tracks.get(position);
        ((ArtistViewHolder)holder).selectTrack = track;
        holder.getImage().setImageBitmap(track.getArtWork());
        holder.getAuthor().setText(track.getAuthor());
    }

    @Override
    public AbstractViewHolder viewHolder(View view) {
        return new ArtistViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    private class ArtistViewHolder extends AbstractViewHolder{

        public Track selectTrack;

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
//            Log.d("TAAA", ""+selectedView.getTitle());
            songCallBack.selectedView(v, selectTrack, tracks, IRowViewPagerInteractionListener.ViewPagerAction.Artist);
        }
    }

}
