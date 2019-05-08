package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.Arrays;

public class LoginPage extends AppCompatActivity {


    private static final String EMAIL = "email";
    private CallbackManager callbackManager=CallbackManager.Factory.create(); //define CallbackManager
    public static LoginButton loginButton;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        startActivity(new Intent(LoginPage.this, com.example.myapplication.MainActivity.class));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(LoginPage.this, com.example.myapplication.MainActivity.class));

                        finish();
                        // App code
                    }
                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });



        // If you are using in a fragment, call loginButton.setFragment(this);
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startActivity(new Intent(LoginPage.this, com.example.myapplication.MainActivity.class));
                finish();
                // App code
                //    textView.setText(accessToken.getToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        if (AccessToken.getCurrentAccessToken()!=null){
            startActivity(new Intent(LoginPage.this, com.example.myapplication.MainActivity.class));
            finish();
        }
    }
    public static void logoutbutton(){

        loginButton.post(new Runnable(){
            @Override
            public void run() {
                loginButton.performClick();

            }
        });

    }
}
