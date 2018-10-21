package com.example.milja.movieapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milja.movieapp.R;
import com.example.milja.movieapp.io.model.FavoriteMessage;
import com.example.milja.movieapp.io.model.MovieCredits;
import com.example.milja.movieapp.io.model.Results;
import com.example.milja.movieapp.io.model.State;
import com.example.milja.movieapp.io.model.TopRated;
import com.example.milja.movieapp.io.retrofit.endpoints.ApiService;
import com.example.milja.movieapp.io.retrofit.endpoints.ApiUtils;
import com.example.milja.movieapp.ui.adapters.CrewAdapter;
import com.example.milja.movieapp.ui.adapters.MainAdapter;
import com.example.milja.movieapp.utils.Constants;
import com.example.milja.movieapp.utils.SharedPref;

import java.util.HashMap;

import me.panpf.sketch.SketchImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsFragment extends BaseFragment {
    private TextView titleTextView, overviewTextView, genreTextVIew;
    private int movieId;
    private SketchImageView posterImageView;
    private CrewAdapter crewAdapter, recommendationsAdapter;
    private ApiService apiService;
    private Button addToFavouritesButton;
    private boolean isFavorite;
    private View.OnClickListener addToFavouritesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeFavoriteState();
        }
    };

    private void changeFavoriteState() {
        apiService.postFavorite(SharedPref.getAccountId(getActivity()), Constants.API_KEY, SharedPref.getSessionId(getActivity()),
                createBody()).enqueue(new Callback<FavoriteMessage>() {
            @Override
            public void onResponse(Call<FavoriteMessage> call, Response<FavoriteMessage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), response.body().getStatusMessage(), Toast.LENGTH_SHORT).show();
                    if (isFavorite) {
                        addToFavouritesButton.setText("Add to your favorite list");
                        isFavorite = true;
                    } else {
                        addToFavouritesButton.setText("Remove from your favorite list");
                        isFavorite = false;
                    }

                }
            }

            @Override
            public void onFailure(Call<FavoriteMessage> call, Throwable t) {

            }
        });
    }

    private HashMap<String, Object> createBody() {
        HashMap<String, Object> body = new HashMap<>();
        body.put("media_type", "movie");
        body.put("media_id", movieId);
        body.put("favorite", !isFavorite);
        return body;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_details, parent, false);
        if (getArguments() != null)
            movieId = getArguments().getInt("item_id");
        apiService = ApiUtils.getApiService();
        initView(v);
        return v;
    }

    private void initView(View view) {
        addToFavouritesButton = view.findViewById(R.id.add_to_favourites_button);
        addToFavouritesButton.setOnClickListener(addToFavouritesListener);
        titleTextView = view.findViewById(R.id.movie_details_title_text_view);
        overviewTextView = view.findViewById(R.id.movie_details_description_text_view);
        posterImageView = view.findViewById(R.id.movie_details_poster_image_view);
        genreTextVIew = view.findViewById(R.id.movie_details_genre_text_view);
        RecyclerView crewRecyclerView = view.findViewById(R.id.crew_recycler_view);
        crewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        crewAdapter = new CrewAdapter(getActivity());
        crewRecyclerView.setAdapter(crewAdapter);


        recommendationsAdapter = new CrewAdapter(getActivity());
        RecyclerView recommendationsRecyclerView = view.findViewById(R.id.recommendations_recycler_view);
        recommendationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recommendationsRecyclerView.setAdapter(recommendationsAdapter);
//potreban progress dialog svuda gde se pravi poziv
        getMoviesDetails();

    }

    private void getMoviesDetails() {

        apiService.getMovieDetails(movieId, Constants.API_KEY).enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setData(response.body());
                    getMovieCrew();
                    getRecommendations();
                    getState();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Results> call, Throwable t) {

            }
        });
    }

    private void getState() {
        apiService.getState(movieId, Constants.API_KEY, SharedPref.getSessionId(getActivity())).enqueue(new Callback<State>() {
            @Override
            public void onResponse(Call<State> call, Response<State> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isFavorite())
                        addToFavouritesButton.setText("Remove from your favorite list");
                    isFavorite = response.body().isFavorite();
                }
            }

            @Override
            public void onFailure(@NonNull Call<State> call, @NonNull Throwable t) {

            }
        });
    }

    private void setData(Results body) {
        StringBuilder genres = new StringBuilder();
        titleTextView.setText(body.getTitle());
        overviewTextView.setText(body.getOverview());
        posterImageView.displayImage(Constants.IMAGE_URL + body.getPosterPath());
//        for (int i = 0; i < body.getGenresRealmList().size(); i++) {
//            genres.append(" ").append(body.getGenresRealmList().get(i));
//        }
//        genreTextVIew.setText(genres.toString());
    }

    private void getRecommendations() {
        apiService.getRecommendations(movieId, Constants.API_KEY).enqueue(new Callback<TopRated>() {
            @Override
            public void onResponse(@NonNull Call<TopRated> call, @NonNull Response<TopRated> response) {
                if (response.isSuccessful() && response.body() != null)
                    recommendationsAdapter.setData(response.body().getResults(), true);
            }

            @Override
            public void onFailure(@NonNull Call<TopRated> call, @NonNull Throwable t) {

            }
        });
    }


    private void getMovieCrew() {
        apiService.getMovieCredits(movieId, Constants.API_KEY).enqueue(new Callback<MovieCredits>() {
            @Override
            public void onResponse(Call<MovieCredits> call, Response<MovieCredits> response) {
                crewAdapter.setData(response.body().getCrew());
            }

            @Override
            public void onFailure(Call<MovieCredits> call, Throwable t) {

            }
        });
    }
}
