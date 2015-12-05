package com.stonewar.appname.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.stonewar.appname.R;
import com.stonewar.appname.model.Song;

import java.util.List;

/**
 * Created by yandypiedra on 10.11.15.
 */
public class SongAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Song> songs;

    public SongAdapter(Context context, List<Song> songs){
        this.context = context;
        this.inflater = android.view.LayoutInflater.from(this.context);
        this.songs =  songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        ViewHolder holder;

        //Test is the view is already created
        if(convertView == null){
            holder = new ViewHolder();
            //create the view
            row = inflater.inflate(R.layout.list_song_row, parent, false);
            holder.image = (ImageView) row.findViewById(R.id.tab_title_image_song);
                holder.title = (TextView) row.findViewById(R.id.tab_title_text_title_song);
                holder.author = (TextView)row.findViewById(R.id.tab_title_text_author_song);
                holder.isSelected = (CheckBox)row.findViewById(R.id.check_box_selected_song);
            row.setTag(holder);
            holder.isSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Song songRow = (Song) cb.getTag();
                    songRow.setIsSelected(cb.isChecked());
                }
            });
        }
        else{
            row = convertView;
            holder = (ViewHolder)row.getTag();
        }

        Song song = songs.get(position);
        holder.image.setImageBitmap(song.getArtWork());
        holder.title.setText(song.getTitle());
        holder.author.setText(song.getAuthor());
        holder.isSelected.setChecked(song.isSelected());
        holder.isSelected.setTag(song);

        return row;
    }

    private class ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView author;
        public CheckBox isSelected;
    }
}
