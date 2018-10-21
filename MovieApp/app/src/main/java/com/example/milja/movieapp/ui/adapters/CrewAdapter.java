package com.example.milja.movieapp.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.milja.movieapp.R;
import com.example.milja.movieapp.io.model.Crew;
import com.example.milja.movieapp.io.model.Results;
import com.example.milja.movieapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import me.panpf.sketch.SketchImageView;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.ViewHolder> {
    private List<Crew> crewList = new ArrayList<>();
    private Activity context;
    private boolean isRecommendations = false;
    private RealmList<Results> recommendationsList;

    public CrewAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crew, parent, false);
        return new ViewHolder(v);
    }

    //dizajn treba mnogo bolje srediti
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!isRecommendations) {
            Crew crew = crewList.get(position);
            holder.nameTextView.setText(crew.getName());
            holder.jobTextView.setText(crew.getJob());
            holder.crewSketchImageView.displayImage(Constants.IMAGE_URL + crew.getProfilePath());
        } else {
            Results results = recommendationsList.get(position);
            holder.nameTextView.setText(results.getTitle());
            if (results.getPosterPath() != null)
                holder.crewSketchImageView.displayImage(Constants.IMAGE_URL + results.getPosterPath());
        }
    }

    @Override
    public int getItemCount() {
        if (isRecommendations)
            return recommendationsList != null ? recommendationsList.size() : 0;
        return crewList != null ? crewList.size() : 0;
    }

    public void setData(List<Crew> dataList) {
        this.crewList = dataList;
        notifyDataSetChanged();
    }

    public void setData(RealmList<Results> dataList, boolean isRecommendations) {
        recommendationsList = dataList;
        this.isRecommendations = isRecommendations;

        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, jobTextView;
        SketchImageView crewSketchImageView;


        /**
         * Adapter view holder
         */
        ViewHolder(View itemView) {
            super(itemView);
            crewSketchImageView = itemView.findViewById(R.id.crew_image);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            jobTextView = itemView.findViewById(R.id.job_text_view);


        }
    }
}
