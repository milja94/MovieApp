package com.example.milja.movieapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.milja.movieapp.R;
import com.example.milja.movieapp.io.model.Session;
import com.example.milja.movieapp.io.model.Token;
import com.example.milja.movieapp.io.model.UserDetails;
import com.example.milja.movieapp.io.retrofit.endpoints.ApiService;
import com.example.milja.movieapp.io.retrofit.endpoints.ApiUtils;
import com.example.milja.movieapp.ui.fragments.MoviesFragment;
import com.example.milja.movieapp.utils.Constants;
import com.example.milja.movieapp.utils.SharedPref;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private ApiService apiService;

//staviti splash koji vrsi provjeru da li je vec ulogovan i na splashu neka animacija
    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validation()) {
                getToken();
            }
        }
    };

    private void login(final String token) {
        apiService.login(Constants.API_KEY, createBody(token)).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful())
                    createSession(token);


            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
    }

    private HashMap<String, Object> createBody(String token) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", usernameEditText.getText().toString());
        body.put("password", passwordEditText.getText().toString());
        body.put("request_token", token);
        return body;
    }

    private void createSession(String token) {
        apiService.createSession(Constants.API_KEY, token).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if (response.isSuccessful()) {
                    SharedPref.setSessionId(LoginActivity.this, response.body().getSessionId());
                    getUserDetails(response.body().getSessionId());


                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {

            }
        });
    }

    private void getUserDetails(String sessionId) {
        apiService.getUserDetails(Constants.API_KEY, sessionId).enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.isSuccessful()) {
                    SharedPref.setAccountId(LoginActivity.this, response.body().getId());
                    goToMain();

                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {

            }
        });
    }

    private void goToMain() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }

    private void getToken() {
        apiService.getToken(Constants.API_KEY).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    SharedPref.setToken(LoginActivity.this, response.body().getRequestToken());
                    login(response.body().getRequestToken());
                }

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
    }

    private boolean validation() {
        if (usernameEditText.getText().toString().equalsIgnoreCase("") || passwordEditText.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Molimo Vas popunite sva polja", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        apiService = ApiUtils.getApiService();
        init();
    }

    private void init() {
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(loginListener);
    }
}
