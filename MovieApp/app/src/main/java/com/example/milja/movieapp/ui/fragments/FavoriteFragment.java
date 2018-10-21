package com.example.milja.movieapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.milja.movieapp.R;
import com.example.milja.movieapp.io.database.Database;
import com.example.milja.movieapp.io.model.FavoriteMessage;
import com.example.milja.movieapp.io.model.Results;
import com.example.milja.movieapp.io.model.TopRated;
import com.example.milja.movieapp.io.retrofit.endpoints.ApiService;
import com.example.milja.movieapp.io.retrofit.endpoints.ApiUtils;
import com.example.milja.movieapp.ui.adapters.MainAdapter;
import com.example.milja.movieapp.utils.Constants;
import com.example.milja.movieapp.utils.NetworkUtils;
import com.example.milja.movieapp.utils.SharedPref;

import java.util.HashMap;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends BaseFragment implements MainAdapter.onItemClickListener {
    private MainAdapter mainAdapter;
    private ApiService apiService;
    private RealmList<Results> favoriteList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_rated, parent, false);
        apiService = ApiUtils.getApiService();
        initView(v);
        return v;
    }

    private void initView(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainAdapter = new MainAdapter(getActivity());
        mainAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mainAdapter);

        getFavorites();
    }

    private void getFavorites() {

        apiService.getFavorites(SharedPref.getAccountId(getActivity()), Constants.API_KEY, SharedPref.getSessionId(getActivity())).enqueue(new Callback<TopRated>() {
            @Override
            public void onResponse(Call<TopRated> call, final Response<TopRated> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainAdapter.setData(response.body().getResults(), true);
                        favoriteList = response.body().getResults();

                    }
                });


            }

            @Override
            public void onFailure(Call<TopRated> call, Throwable t) {
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int item_id, int position) {
        changeFavoriteState(item_id, position);
    }

    private void changeFavoriteState(int movieId, final int position) {
        apiService.postFavorite(SharedPref.getAccountId(getActivity()), Constants.API_KEY, SharedPref.getSessionId(getActivity()),
                createBody(movieId)).enqueue(new Callback<FavoriteMessage>() {
            @Override
            public void onResponse(Call<FavoriteMessage> call, Response<FavoriteMessage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), response.body().getStatusMessage(), Toast.LENGTH_SHORT).show();
                    favoriteList.remove(position);
                    mainAdapter.setData(favoriteList, true);
                }
            }

            @Override
            public void onFailure(Call<FavoriteMessage> call, Throwable t) {

            }
        });
    }

    private HashMap<String, Object> createBody(int movieId) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("media_type", "movie");
        body.put("media_id", movieId);
        body.put("favorite", false);
        return body;
    }
}
