package com.example.milja.movieapp.ui.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.milja.movieapp.R;
import com.example.milja.movieapp.io.model.Results;
import com.example.milja.movieapp.io.model.TopRated;
import com.example.milja.movieapp.ui.activities.MainActivity;
import com.example.milja.movieapp.ui.fragments.MovieDetailsFragment;
import com.example.milja.movieapp.utils.Constants;

import io.realm.RealmList;
import me.panpf.sketch.SketchImageView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Activity context;
    private RealmList<Results> topRatedRealmList = new RealmList<>();
    private boolean isFavorite = false;

    public MainAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Results topRated = topRatedRealmList.get(position);
        holder.titleTextView.setText(topRated.getTitle());
        holder.genreTextView.setText("comedy");
        holder.ratingTextView.setText(String.valueOf(topRated.getVoteAverage()));
        holder.posterSketchImageView.displayImage(Constants.IMAGE_URL + topRated.getPosterPath());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMovieDetails(topRated.getId());
            }
        });
        if(isFavorite) {
            holder.favoriteImageView.setVisibility(View.VISIBLE);
            holder.favoriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(topRated.getId(), position);
                }
            });
        }else
            holder.favoriteImageView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return topRatedRealmList != null ? topRatedRealmList.size() : 0;
    }

    public void setData(RealmList<Results> dataList, boolean isFavorite) {
        this.topRatedRealmList = dataList;
        this.isFavorite = isFavorite;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ratingTextView, titleTextView, genreTextView;
        SketchImageView posterSketchImageView;
        LinearLayout parentLayout;
        ImageView favoriteImageView;

        /**
         * Adapter view holder
         */
        ViewHolder(View itemView) {
            super(itemView);

            posterSketchImageView = itemView.findViewById(R.id.poster_image_view);
            ratingTextView = itemView.findViewById(R.id.rating_text_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            genreTextView = itemView.findViewById(R.id.genre_text_view);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            favoriteImageView=itemView.findViewById(R.id.favorite_image_view);


        }
    }

    private void goToMovieDetails(int id) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle b = new Bundle();
        b.putInt("item_id", id);
        fragment.setArguments(b);
        ((MainActivity) context).onSwitchFragment(fragment, Constants.FR_MOVIE_DETAILS);
    }

    public interface onItemClickListener {
        void onItemClick(int item_id, int position);
    }

    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(onItemClickListener l) {
        onItemClickListener = l;
    }
}
