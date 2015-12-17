package com.stonewar.appname.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stonewar.appname.model.Track;

/**
 * Created by yandypiedra on 05.12.15.
 */
public abstract class AbstractViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    protected ImageView image;
    protected TextView author;

    public AbstractViewHolder(View vRow) {
        super(vRow);
        vRow.setClickable(true);
        vRow.setOnClickListener(this);
        image = (ImageView) vRow.findViewById(imageViewById());
        author = (TextView) vRow.findViewById(authorViewById());
    }

    public abstract int imageViewById();

    public abstract int authorViewById();

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public TextView getAuthor() {
        return author;
    }

    public void setAuthor(TextView author) {
        this.author = author;
    }
}