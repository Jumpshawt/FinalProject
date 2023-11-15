package com.example.finalproject.ui.home;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView time, name, location, org ;
    CardView holder;

    Button join, like, share;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.image);
        time = itemView.findViewById(R.id.event_time);
        name = itemView.findViewById(R.id.event_name);
        location = itemView.findViewById(R.id.event_location);
        org = itemView.findViewById(R.id.event_org);
        holder = itemView.findViewById(R.id.event_container);

        join = itemView.findViewById(R.id.join);
        like = itemView.findViewById(R.id.like);
        share = itemView.findViewById(R.id.share);
    }

    public ImageView getImage() {
        return image;
    }

    public TextView getTime() {
        return time;
    }

    public TextView getName() {
        return name;
    }

    public TextView getLocation() {
        return location;
    }

    public TextView getOrg() {
        return org;
    }

    public CardView getHolder() {
        return holder;
    }

    public Button getJoin() {
        return join;
    }

    public Button getLike() {
        return like;
    }

    public Button getShare() {
        return share;
    }
}
