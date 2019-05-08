
package com.example.myapplication;
import java9.util.concurrent.CompletableFuture;
import org.json.*;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;


import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
//import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationHandler;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import okhttp3.OkHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {
    public void removefromFavorites(){
        int indx = s.getSelectedItemPosition();
        Log.d("TEST","----------------------------"+indx+"++++");
        String id;
        if (suggest&&indx>0){
            id=songIDs.get(indx-1);
        }
        else{
            id=selectedid;
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody formbody=new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                //.url("https://api.spotify.com/v1/playlists")
                //.post(formbody)
                .url("https://api.spotify.com/v1/me/tracks?ids="+id)
                .delete(formbody)  //or put to add track to favorites
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+SpotifyToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    Log.d("Debug"," LIKEDDDDDDDDDDDD +++++////////////");
                }
            }




        });
        suggest=true;

    }

    public void AddToFavorites(){
        int indx = s.getSelectedItemPosition();
        Log.d("TEST","----------------------------"+indx+"++++");
        String id;
        if (suggest&&indx>0){
            id=songIDs.get(indx-1);
        }
        else{
           id=selectedid;
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody formbody=new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                //.url("https://api.spotify.com/v1/playlists")
                //.post(formbody)
                .url("https://api.spotify.com/v1/me/tracks?ids="+id)
                .put(formbody)  //or put to add track to favorites
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+SpotifyToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    Log.d("Debug"," LIKEDDDDDDDDDDDD +++++////////////");
                }
            }




        });
        suggest=true;

    }

/****************************************
 *                                      *
 *                                      *
 *                                      *
 *                                      *
 * Songs API
 *                                      *
 *                                      *
 *                                      *
 *                                      *
 ****************************************/
    boolean suggest=false;
    public  String encodeField(String in)
    {
        // usually, this would be
        //  return java.net.URLEncoder.encode(in, "UTF-8");
        // but this site *appears* to use a non-standard mapping
        try {
            return java.net.URLEncoder.encode(in.replace(' ', '_'), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    return "";
    }
    public String getLyricsText(int i) throws IOException {
        // construct the REST query URL
        String artist,song;
        int indx = s.getSelectedItemPosition();
        Log.d("TEST","----------------------------"+indx+"++++");
        if ((suggest&&indx>0)||i==1){
             song=currentSongs.get(indx-1);
             artist=currentArtist.get(indx-1);
        }
        else {
             song=selectedSong;
             artist=searchartist;
        }
        Log.d("SEARCHER",song+"++++++++++++++++++++++++++++++++++"+artist);
        String query = "http://lyrics.wikia.com/api.php?func=getSong&artist="
                + encodeField(artist)
                + "&song="
                + encodeField(song)
                + "&fmt=text";
        URL url = null;
        try {
            url = new URL(query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        java.io.InputStream is = null;
        try {
            is = url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get the text from the stream as lines
       BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder buf = new StringBuilder();
        String s;
        while ( (s = reader.readLine()) != null )
            buf.append(s).append('\n');
        // return the lines
        suggest=true;

        return buf.toString();
    }


    public  Spinner s;

    public String selectedSong="",selectedid,searchartist;
    public Vector<String> currentSongs=new Vector<>(),currentArtist=new Vector<>(),vector=new Vector<>(),songIDs=new Vector<>();
    private SpotifyAppRemote mSpotifyAppRemote;
    private static final String EMAIL = "email";
    private CallbackManager callbackManager=CallbackManager.Factory.create(); //define CallbackManager
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    public boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    String SpotifyToken="";
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "https://myswe2medeor/callback/";
    public TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    textView= (TextView) findViewById(R.id.textView);





























        vector.add(" ");
        s = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, vector);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s.setAdapter(adapter);


        /////////////////////////////////////////////////////////////////////////
































        Button Music = (Button) findViewById(R.id.ShowMusic);
        Music.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggest=false;
                currentArtist.removeAllElements();
                currentSongs.removeAllElements();
                selectedSong="";
                if (isLoggedIn) {
                    new GraphRequest(

                            accessToken,
                            "/" + accessToken.getUserId() + "/music",
                            null,
                            HttpMethod.GET,
                            new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {
Log.d("Debuger","D:                     "+accessToken.getToken());
                                    /* handle the result */
                                    try {
                                        JSONObject obj = new JSONObject(response.getRawResponse());
                                        JSONArray arr = obj.getJSONArray("data");
                                        textView.setText("");
                                        if (arr.length()>0){
                                            currentArtist.removeAllElements();
                                            currentSongs.removeAllElements();
                                            songIDs.removeAllElements();
                                            selectedSong = "";
                                            vector.removeAllElements();}
                                        for (int i = 0; i < arr.length(); i++) {
                                            String name = arr.getJSONObject(i).getString("name");
                                            textView.append(name+"\n");
                                            currentArtist.add(name);
                                        }
                                    } catch (JSONException e) {
                                    }
                                }
                            }
                    ).executeAsync();

                }
                else{
                    textView.setText("ZFTTTTTTTTTTTTTTTTTT");
                }
            }
        });


        Button Funct = (Button) findViewById(R.id.Post);
        Funct.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToFavorites();
            }
        });
        Button undo = (Button) findViewById(R.id.Undo);
        undo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removefromFavorites();
            }
        });
        Button lyrics = (Button) findViewById(R.id.lyrics);
        lyrics.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {String s=getLyricsText(0);
                            Log.d("LYRICS ","-------+++++-----------------------------------------------------"+s);
                            Thread thread = new Thread(){
                                @Override
                                public void run() {
                                    synchronized (this) {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                textView.setText("Lyrics:" +s);
                                            }
                                        });

                                    }

                                };
                            };
                            thread.start();
                            //Your code goes here
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

            }
        });

        Button Search = (Button) findViewById(R.id.Search);
        Search.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isLoggedIn&&currentSongs.isEmpty()) {
                    new GraphRequest(

                            accessToken,
                            "/" + accessToken.getUserId() + "/music",
                            null,
                            HttpMethod.GET,
                            new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {
                                    //                 Log.d("Debuger","D:                     "+accessToken.getToken());
                                    /* handle the result */
                                    try {
                                        JSONObject obj = new JSONObject(response.getRawResponse());
                                        JSONArray arr = obj.getJSONArray("data");
                                        //                       textView.setText("");
                                        if (arr.length()>0){
                                            suggest=true;
                                            currentArtist.removeAllElements();
                                            currentSongs.removeAllElements();
                                            songIDs.removeAllElements();
                                            selectedSong = "";

                                            vector.removeAllElements();
                                        vector.add("");}
                                        for (int i = 0; i < 3; i++) {

                                            String artistname = arr.getJSONObject(i).getString("name");
                                            //currentArtist.add(artistname);
                                            Log.d("Zft","---------------------------------------------------------------------------------"+artistname);
                                            OkHttpClient client = new OkHttpClient();
                                            Log.d("ArtistName","----------------------------------------------------------"+artistname);
                                            Request request = new Request.Builder()
                                                    .url("https://api.spotify.com/v1/search?q="+conversion(artistname)+"&type=artist")
                                                    .get()
                                                    .addHeader("Accept", "application/json")
                                                    .addHeader("Content-Type", "application/json")
                                                    .addHeader("Authorization", "Bearer " + SpotifyToken)
                                                    .build();
                                            client.newCall(request).enqueue(new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    e.printStackTrace();
                                                }
                                                @Override
                                                public void onResponse(Call call, final Response response) throws IOException {
                                                    if (!response.isSuccessful()) {
                                                        throw new IOException("Unexpected code " + response);
                                                    } else {
                                                        try {
                                                            String responseString = response.body().string();
                                                            JSONObject json = new JSONObject(responseString);
                                                            JSONObject json_LL = json.getJSONObject("artists");
                                                            JSONArray jr = json_LL.getJSONArray("items"); //<< get value here
                                                            if (jr.length()>0&&jr!=null) {
                                                                JSONObject jb = (JSONObject) jr.getJSONObject(0);
                                                                String idartist = jb.getString("id");
                                                                OkHttpClient client = new OkHttpClient();
                                                                Request request = new Request.Builder()
                                                                        .url("https://api.spotify.com/v1/artists/" + idartist + "/top-tracks?country=US")
                                                                        .get()
                                                                        .addHeader("Accept", "application/json")
                                                                        .addHeader("Content-Type", "application/json")
                                                                        .addHeader("Authorization", "Bearer " + SpotifyToken)
                                                                        .build();
                                                                client.newCall(request).enqueue(new Callback() {
                                                                    @Override
                                                                    public void onFailure(Call call, IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    @Override
                                                                    public void onResponse(Call call, final Response response) throws IOException {
                                                                        if (!response.isSuccessful()) {
                                                                            throw new IOException("Unexpected code " + response);
                                                                        } else {
                                                                            try {
                                                                                String responseString = response.body().string();
                                                                                Log.d("Debug", "Here22222222222222------------------------" + responseString);
                                                                                JSONObject json = new JSONObject(responseString);
                                                                                JSONArray jr = json.getJSONArray("tracks"); //<< get value here
                                                                                if (jr.length()>0&&jr!=null) { JSONObject jb = (JSONObject) jr.getJSONObject(0);
                                                                                    String id = jb.getString("id");
                                                                                    String name = jb.getString("name");
                                                                                    Thread thread = new Thread() {
                                                                                        @Override
                                                                                        public void run() {
                                                                                            synchronized (this) {

                                                                                                runOnUiThread(new Runnable() {
                                                                                                    @Override
                                                                                                    public void run() {

                                                                                                        currentArtist.add(artistname);
                                                                                                        currentSongs.add(name);
                                                                                                        vector.add(artistname+" top hit: "+name);
                                                                                                        songIDs.add(id);
                                                                                                        Log.d("DEBUGGGGGGGGGGG","ID:" + id + "-------------" + "name" + name);
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }

                                                                                        ;
                                                                                    };
                                                                                    thread.start();}
                                                                                else {
                                                                                    currentSongs.add("songs not found");
                                                                                }
                                                                                // Log.d("Lyrics","----------------->>"+getLyricsText("Bruno Mars","finesse"));
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                        // do something wih the result
                                                                    }
                                                                });
                                                                //  Log.d("Debug","Herrrrrrrrrre------------------------"+id);
                                                            }else{
                                                                currentSongs.add("songs not found");
                                                            }
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                    }
                                }
                            }
                    ).executeAsync();
                } else {
                    textView.setText("ZFTTTTTTTTTTTTTTTTTT");
                }
                for (String adder:vector){
                    adapter.add(adder);
                    adapter.notifyDataSetChanged();
                }
            }});
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder("e501456e13294a1ea27c543177802384", AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"playlist-read-collaborative","user-follow-modify","streaming","user-library-modify","user-library-read","user-follow-read","user-read-private","app-remote-control","playlist-modify-public","playlist-read-private","playlist-modify-private"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }
    public static String conversion(String str){
        String text = str.replace(" ","%20");
        return text;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.searcher).getActionView();
       /* searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
       */
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // This is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                s.setSelection(0);
                OkHttpClient client = new OkHttpClient();
                Log.d("conversion","*********************"+conversion(query));
                Request request = new Request.Builder()
                        .url("https://api.spotify.com/v1/search?q="+conversion(query)+"&type=artist")
                        .get()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer "+SpotifyToken)
                        .build();

                //   final Response[] ans = new Response[1];
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else {
                            try {
                                String responseString = response.body().string();
                                //   Log.d("Debug","Here------------------------"+responseString);
                                JSONObject json = new JSONObject(responseString);
                                // get LL json object
                                JSONObject json_LL = json.getJSONObject("artists");
                                JSONArray jr = json_LL.getJSONArray("items"); //<< get value here
                                if (jr.length()>0&&jr!=null) {
                                    JSONObject jb = (JSONObject) jr.getJSONObject(0);
                                    String idartist = jb.getString("id");
                                    searchartist=query;
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url("https://api.spotify.com/v1/artists/" + idartist + "/top-tracks?country=US")
                                            .get()
                                            .addHeader("Accept", "application/json")
                                            .addHeader("Content-Type", "application/json")
                                            .addHeader("Authorization", "Bearer " + SpotifyToken)
                                            .build();
                                    client.newCall(request).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            e.printStackTrace();
                                        }
                                        @Override
                                        public void onResponse(Call call, final Response response) throws IOException {
                                            if (!response.isSuccessful()) {
                                                throw new IOException("Unexpected code " + response);
                                            } else {
                                                try {
                                                    String responseString = response.body().string();
                                                    Log.d("Debug", "Here22222222222222------------------------" + responseString);
                                                    JSONObject json = new JSONObject(responseString);

                                                    // get LL json object

                                                    JSONArray jr = json.getJSONArray("tracks"); //<< get value here
                                                    if (jr.length()>0&&jr!=null) {
                                                    suggest = false;
                                                    selectedSong = "";
                                                    JSONObject jb = (JSONObject) jr.getJSONObject(0);
                                                    String id = jb.getString("id");
                                                    String name = jb.getString("name");
                                                  //  currentSongs.add(id);
                                                    selectedSong = name;
                                                    selectedid=id;
                                                    Thread thread = new Thread() {
                                                        @Override
                                                        public void run() {
                                                            synchronized (this) {

                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        textView.setText(query + " top hit: " + name);
                                                                    }
                                                                });

                                                            }

                                                        }


                                                    };
                                                    thread.start();
                                                }
                                                else{}
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                // do something wih the result
                                            }
                                        }


                                    });


                                }
                            else {

                                Thread thread = new Thread() {
                                        @Override
                                        public void run() {
                                            synchronized (this) {

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        textView.setText(query+" not found ");
                                                    }
                                                });

                                            }

                                        }

                                        ;
                                    };
                                    thread.start();

                                }} catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }}});
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
//        return true;
    }
	/*
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.searcher) {
           // LoginButton.
            Log.d("Search","here+++++++++++++++");
        //    SearchResultActivity a=new SearchResultActivity();
            return true;
        }
        else if (id==R.id.logout){
            Log.d("LOGOUT","^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

            LoginManager.getInstance().logOut();
            startActivity(new Intent( MainActivity.this,LoginPage.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    SpotifyToken=response.getAccessToken();
                    Log.d("DEBUG","abos 2edek ya shekh: ------------ "+SpotifyToken);
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
	*/
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.searcher) {
           // LoginButton.
            Log.d("Search","here+++++++++++++++");
        //    SearchResultActivity a=new SearchResultActivity();
            return true;
        }
        else if (id==R.id.logout){
            Log.d("LOGOUT","^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

            LoginManager.getInstance().logOut();
            startActivity(new Intent( MainActivity.this,LoginPage.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    SpotifyToken=response.getAccessToken();
                    Log.d("DEBUG","abos 2edek ya shekh: ------------ "+SpotifyToken);
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
	

}
