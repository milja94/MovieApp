package com.example.milja.movieapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.milja.movieapp.R;
import com.example.milja.movieapp.io.database.Database;
import com.example.milja.movieapp.io.model.Results;
import com.example.milja.movieapp.io.model.TopRated;
import com.example.milja.movieapp.io.retrofit.endpoints.ApiService;
import com.example.milja.movieapp.io.retrofit.endpoints.ApiUtils;
import com.example.milja.movieapp.ui.adapters.MainAdapter;
import com.example.milja.movieapp.utils.Constants;
import com.example.milja.movieapp.utils.NetworkUtils;
import com.google.gson.Gson;

import java.util.Locale;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopRatedFragment extends BaseFragment {

    private MainAdapter mainAdapter;
    private RealmList<Results> searchList = new RealmList<>();
    private RealmList<Results> topRatedList;

    private View.OnClickListener favoriteListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switchFragment(new FavoriteFragment(),Constants.FR_FAVORITE);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_rated, parent, false);

        initView(v);
        return v;
    }

    private void initView(View view) {
        EditText searchEditText = getActivity().findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(textWatcher);
        ImageView addToFavorite=getActivity().findViewById(R.id.favorite_image_view);
        addToFavorite.setOnClickListener(favoriteListener);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainAdapter = new MainAdapter(getActivity());
        recyclerView.setAdapter(mainAdapter);
        if (!NetworkUtils.hasInternetAccess(getActivity(), false)) {
            if (Database.getTopRatedMoviesString().size() > 0) {
                mainAdapter.setData(Database.getTopRatedMoviesString(),false);
                topRatedList = Database.getTopRatedMoviesString();
            }
        } else
            getTopRatedMovies();

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 2) {
                filter(s.toString());
            } else if (s.length() == 0) {
                mainAdapter.setData(topRatedList,false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void getTopRatedMovies() {
        ApiService apiService = ApiUtils.getApiService();
        apiService.getTopRatedMovies(Constants.API_KEY, "en-US", "1").enqueue(new Callback<TopRated>() {
            @Override
            public void onResponse(Call<TopRated> call, final Response<TopRated> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful() && response.body() != null)
                            Database.deleteListDataFromLocale();
                        Database.storeListDataToServer(response.body().getResults());
                        topRatedList = response.body().getResults();
                        mainAdapter.setData(response.body().getResults(),false);

                        Toast.makeText(getActivity(), "ddsdfsd", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onFailure(Call<TopRated> call, Throwable t) {
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String charText) {
        String name;
        charText = charText.toLowerCase(Locale.getDefault());
        searchList.clear();
        if (charText.length() == 0) {
            searchList.addAll(topRatedList);
        } else {
            for (int i = 0; i < topRatedList.size(); i++) {
                name = topRatedList.get(i).getTitle();
                if (name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchList.add(topRatedList.get(i));

                }
            }
        }
        mainAdapter.setData(searchList,false);
    }
}
