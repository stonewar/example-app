package com.stonewar.appname.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stonewar.appname.R;
import com.stonewar.appname.model.Track;

import java.util.List;

/**
 * Created by yandypiedra on 18.12.15.
 */
public class AlbumArtistAdapter extends RecyclerView.Adapter<AlbumArtistAdapter.ViewHolder> {

    private List<Track> tracks;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlbumArtistAdapter(List<Track> tracks) {
        this.tracks = tracks;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlbumArtistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_artitst_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Track track = tracks.get(position);
        holder.title.setText(track.getTitle());
        holder.albumOrAuthor.setText(track.getAuthor()); //TODO when should be set the author an when the album
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView albumOrAuthor;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title_track);
            albumOrAuthor = (TextView) v.findViewById(R.id.album_or_author_track);
        }
    }
}
