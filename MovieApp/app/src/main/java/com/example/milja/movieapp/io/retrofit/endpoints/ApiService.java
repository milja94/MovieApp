package com.example.milja.movieapp.io.retrofit.endpoints;

import com.example.milja.movieapp.io.model.FavoriteMessage;
import com.example.milja.movieapp.io.model.MovieCredits;
import com.example.milja.movieapp.io.model.Results;
import com.example.milja.movieapp.io.model.Session;
import com.example.milja.movieapp.io.model.State;
import com.example.milja.movieapp.io.model.Token;
import com.example.milja.movieapp.io.model.TopRated;
import com.example.milja.movieapp.io.model.UserDetails;

import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("movie/top_rated")
    Call<TopRated> getTopRatedMovies(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") String page
    );

    @GET("movie/{movie_id}/credits")
    Call<MovieCredits> getMovieCredits(
            @Path("movie_id") int id,
            @Query("api_key") String api_key
    );


    @GET("movie/{movie_id}")
    Call<Results> getMovieDetails(
            @Path("movie_id") int id,
            @Query("api_key") String api_key
    );

    @GET("movie/{movie_id}/recommendations")
    Call<TopRated> getRecommendations(
            @Path("movie_id") int id,
            @Query("api_key") String api_key
    );

    @GET("authentication/token/new")
    Call<Token> getToken(
            @Query("api_key") String api_key
    );

    @POST("authentication/token/validate_with_login")
    Call<Token> login(
            @Query("api_key") String api_key,
            @Body HashMap<String, Object> params
    );

@FormUrlEncoded
    @POST("authentication/session/new")
    Call<Session> createSession(
            @Query("api_key") String api_key,
            @Field("request_token") String request_token
    );

    @GET("account")
    Call<UserDetails> getUserDetails(
            @Query("api_key") String api_key,
            @Query("session_id") String session_id
    );

    @GET("movie/{movie_id}/account_states")
    Call<State> getState(
            @Path("movie_id") int movieId,
            @Query("api_key") String api_key,
            @Query("session_id") String session_id
    );

    @POST("account/{account_id}/favorite")
    Call<FavoriteMessage> postFavorite(
            @Path("account_id") int account_id,
            @Query("api_key") String api_key,
            @Query("session_id") String session_id,
            @Body HashMap<String, Object> params
    );

    @GET("account/{account_id}/favorite/movies")
    Call<TopRated> getFavorites(
            @Path("account_id") int account_id,
            @Query("api_key") String api_key,
            @Query("session_id") String session_id
    );
}
