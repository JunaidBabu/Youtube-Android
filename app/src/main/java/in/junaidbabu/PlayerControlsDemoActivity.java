/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.junaidbabu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.PlaylistEventListener;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple YouTube Android API demo application demonstrating the use of {@link YouTubePlayer}
 * programmatic controls.
 */
public class PlayerControlsDemoActivity extends YouTubeFailureRecoveryActivity implements
        View.OnClickListener,
        TextView.OnEditorActionListener,
        CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener {

    private static final ListEntry[] ENTRIES = {
            new ListEntry("Androidify App", "irH3OSOskcE", false),
            new ListEntry("Chrome Speed Tests", "nCgQDjiotG0", false),
            //new ListEntry("Playlist: Google I/O 2012", "PL56D792A831D0C362", true)
    };

    private String ACCESS_TOKEN;
    private String url_user = "https://gdata.youtube.com/feeds/api/users/default?v=2&key="+DeveloperKey.DEVELOPER_KEY+"&alt=json&access_token=";
    private String url_recommendations = "https://gdata.youtube.com/feeds/api/users/default/recommendations?v=2&key="+DeveloperKey.DEVELOPER_KEY+"&alt=json&access_token=";

    private static final String KEY_CURRENTLY_SELECTED_ID = "currentlySelectedId";

    private YouTubePlayerView youTubePlayerView, youTubePlayerView2;
    private YouTubePlayer player;
    private TextView stateText;
    private ArrayAdapter<ListEntry> videoAdapter;
    private Spinner videoChooser;
    private Button playButton;
    private Button pauseButton;
    private Button reloadButton;
    private EditText skipTo;
    private TextView eventLog;
    private StringBuilder logString;
    private RadioGroup styleRadioGroup;

    private MyPlaylistEventListener playlistEventListener;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;

    private int currentlySelectedPosition;
    private String currentlySelectedId;

    private TextView username;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.player_controls_demo);
        SharedPreferences status = getSharedPreferences("TOKEN", 0);
        ACCESS_TOKEN = status.getString("token", null);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        youTubePlayerView2 = (YouTubePlayerView) findViewById(R.id.youtube_view2);

        new GetUserInfo().execute(url_user+ACCESS_TOKEN);
        new GetUserReco().execute(url_recommendations+ACCESS_TOKEN);

        username = (TextView) findViewById(R.id.username);
        stateText = (TextView) findViewById(R.id.state_text);
        videoChooser = (Spinner) findViewById(R.id.video_chooser);
        playButton = (Button) findViewById(R.id.play_button);
        pauseButton = (Button) findViewById(R.id.pause_button);
        reloadButton = (Button) findViewById(R.id.reload_button);
        skipTo = (EditText) findViewById(R.id.skip_to_text);
        eventLog = (TextView) findViewById(R.id.event_log);

        styleRadioGroup = (RadioGroup) findViewById(R.id.style_radio_group);
        ((RadioButton) findViewById(R.id.style_default)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.style_minimal)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.style_chromeless)).setOnCheckedChangeListener(this);
        logString = new StringBuilder();

        videoAdapter = new ArrayAdapter<ListEntry>(this, android.R.layout.simple_spinner_item, ENTRIES);
        videoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        videoChooser.setOnItemSelectedListener(this);
        videoChooser.setAdapter(videoAdapter);

        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        reloadButton.setOnClickListener(this);
        skipTo.setOnEditorActionListener(this);


       // youTubePlayerView2.initialize(DeveloperKey.DEVELOPER_KEY, this);
        PlayerControlsDemoActivity.this.runOnUiThread(new Runnable(){
            public void run(){
                Log.d("UI thread", "I am the UI thread");
                Toast.makeText(PlayerControlsDemoActivity.this, "Hola", Toast.LENGTH_SHORT).show();
                youTubePlayerView.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider1, YouTubePlayer player1, boolean restored1) {
                        if (!restored1) {
                            player1.cueVideo("Daa38ruXHxE");
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider1, YouTubeInitializationResult result1) {
                    }
                });
                //youTubePlayerView.initialize(DeveloperKey.DEVELOPER_KEY, PlayerControlsDemoActivity.this);
            }
        });

        PlayerControlsDemoActivity.this.runOnUiThread(new Runnable(){
            public void run(){
                Log.d("UI thread", "I am the UI thread");
                Toast.makeText(PlayerControlsDemoActivity.this, "Hola2", Toast.LENGTH_SHORT).show();
                youTubePlayerView2.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean restored) {

                        if (!restored) {
                            player.loadVideo("ctQAPiojDKE");
                        }
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider1, YouTubeInitializationResult result1) {
                    }
                });
            }
        });



        playlistEventListener = new MyPlaylistEventListener();
        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();

        setControlsEnabled(false);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        this.player = player;
        player.setPlaylistEventListener(playlistEventListener);
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);

        if (!wasRestored) {
            playVideoAtSelection();
        }
        setControlsEnabled(true);
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubePlayerView;
    }

    private void playVideoAtSelection() {
        ListEntry selectedEntry = videoAdapter.getItem(currentlySelectedPosition);
        if (selectedEntry.id != currentlySelectedId && player != null) {
            currentlySelectedId = selectedEntry.id;
            if (selectedEntry.isPlaylist) {
                player.cuePlaylist(selectedEntry.id);
            } else {
                player.cueVideo(selectedEntry.id);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        currentlySelectedPosition = pos;
        playVideoAtSelection();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }

    @Override
    public void onClick(View v) {
        if (v == playButton) {
            player.play();
        } else if (v == pauseButton) {
            player.pause();
        } else if(v == reloadButton){
            Toast.makeText(this, "Refreshing recommendation list", Toast.LENGTH_SHORT).show();
            new GetUserReco().execute(url_recommendations+ACCESS_TOKEN);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v == skipTo) {
            int skipToSecs = parseInt(skipTo.getText().toString(), 0);
            player.seekToMillis(skipToSecs * 1000);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(skipTo.getWindowToken(), 0);
            return true;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked && player != null) {
            switch (buttonView.getId()) {
                case R.id.style_default:
                    player.setPlayerStyle(PlayerStyle.DEFAULT);
                    break;
                case R.id.style_minimal:
                    player.setPlayerStyle(PlayerStyle.MINIMAL);
                    break;
                case R.id.style_chromeless:
                    player.setPlayerStyle(PlayerStyle.CHROMELESS);
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(KEY_CURRENTLY_SELECTED_ID, currentlySelectedId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        currentlySelectedId = state.getString(KEY_CURRENTLY_SELECTED_ID);
    }

    private void updateText() {
        stateText.setText(String.format("Current state: %s %s %s",
                playerStateChangeListener.playerState, playbackEventListener.playbackState,
                playbackEventListener.bufferingState));
    }

    private void log(String message) {
        logString.append(message + "\n");
        eventLog.setText(logString);
    }

    private void setControlsEnabled(boolean enabled) {
        playButton.setEnabled(enabled);
        pauseButton.setEnabled(enabled);
        skipTo.setEnabled(enabled);
        videoChooser.setEnabled(enabled);
        for (int i = 0; i < styleRadioGroup.getChildCount(); i++) {
            styleRadioGroup.getChildAt(i).setEnabled(enabled);
        }
    }

    private static final int parseInt(String intString, int defaultValue) {
        try {
            return intString != null ? Integer.valueOf(intString) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? "" : hours + ":")
                + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }

    private String getTimesText() {
        int currentTimeMillis = player.getCurrentTimeMillis();
        int durationMillis = player.getDurationMillis();
        return String.format("(%s/%s)", formatTime(currentTimeMillis), formatTime(durationMillis));
    }

    private final class MyPlaylistEventListener implements PlaylistEventListener {
        @Override
        public void onNext() {
            log("NEXT VIDEO");
        }

        @Override
        public void onPrevious() {
            log("PREVIOUS VIDEO");
        }

        @Override
        public void onPlaylistEnded() {
            log("PLAYLIST ENDED");
        }
    }

    private final class MyPlaybackEventListener implements PlaybackEventListener {
        String playbackState = "NOT_PLAYING";
        String bufferingState = "";
        @Override
        public void onPlaying() {
            playbackState = "PLAYING";
            updateText();
            log("\tPLAYING " + getTimesText());

        }

        @Override
        public void onBuffering(boolean isBuffering) {
            bufferingState = isBuffering ? "(BUFFERING)" : "";
            updateText();
            log("\t\t" + (isBuffering ? "BUFFERING " : "NOT BUFFERING ") + getTimesText());
        }

        @Override
        public void onStopped() {
            playbackState = "STOPPED";
            updateText();
            log("\tSTOPPED");
            //player.play();
        }

        @Override
        public void onPaused() {
            playbackState = "PAUSED";
            updateText();
            log("\tPAUSED " + getTimesText());
        }

        @Override
        public void onSeekTo(int endPositionMillis) {
            log(String.format("\tSEEKTO: (%s/%s)",
                    formatTime(endPositionMillis),
                    formatTime(player.getDurationMillis())));
        }
    }

    private final class MyPlayerStateChangeListener implements PlayerStateChangeListener {
        String playerState = "UNINITIALIZED";

        @Override
        public void onLoading() {
            playerState = "LOADING";
            updateText();
            log(playerState);
        }

        @Override
        public void onLoaded(String videoId) {
            playerState = String.format("LOADED %s", videoId);
            updateText();
            log(playerState);
            player.play();

        }

        @Override
        public void onAdStarted() {
            playerState = "AD_STARTED";
            updateText();
            log(playerState);
        }

        @Override
        public void onVideoStarted() {
            playerState = "VIDEO_STARTED";
            updateText();
            log(playerState);
        }

        @Override
        public void onVideoEnded() {
            playerState = "VIDEO_ENDED";
            updateText();
            log(playerState);
            currentlySelectedPosition++;
            playVideoAtSelection();

        }

        @Override
        public void onError(ErrorReason reason) {
            playerState = "ERROR (" + reason + ")";
            if (reason == ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) {
                // When this error occurs the player is released and can no longer be used.
                player = null;
                setControlsEnabled(false);


            }
            updateText();
            log(playerState);

            currentlySelectedPosition++;
            playVideoAtSelection();
        }

    }

    private static final class ListEntry {

        public final String title;
        public final String id;
        public final boolean isPlaylist;

        public ListEntry(String title, String videoId, boolean isPlaylist) {
            this.title = title;
            this.id = videoId;
            this.isPlaylist = isPlaylist;
        }

        @Override
        public String toString() {
            return title;
        }

    }

    private class GetUserInfo extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                int sc = con.getResponseCode();
                if (sc == 200) {
                    InputStream is = con.getInputStream();
                    String json = readResponse(is);
                    is.close();
                    JSONObject profileData = new JSONObject(json);
                    //Log.e("Json Output this is", profileData.getJSONObject("entry").getJSONObject("title").getString("$t").toString());
                    username.setText("Logged in as " + profileData.getJSONObject("entry").getJSONObject("title").getString("$t"));
                }
            } catch (Exception e) {
               // e.printStackTrace();
            }

            return null;
        }
    }
    private class GetUserReco extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            URL url = null;
            JSONObject data = null;
            try {
                url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                int sc = con.getResponseCode();
                if (sc == 200) {
                    InputStream is = con.getInputStream();
                    String json = readResponse(is);
                    is.close();
                    data = new JSONObject(json);
                    //username.setText("Logged in as " + profileData.getJSONObject("entry").getJSONObject("title").getString("$t"));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }
        @Override
        protected void onPostExecute(JSONObject result) {
            //Log.i("result", result);
            try {
                Log.e("Video Title", result.getJSONObject("feed").getJSONArray("entry").getJSONObject(0).getJSONObject("title").getString("$t"));
                Log.e("Video id", result.getJSONObject("feed").getJSONArray("entry").getJSONObject(0).getJSONObject("media$group").getJSONObject("yt$videoid").getString("$t"));

                ListEntry[] ENTRIES = new ListEntry[25];
                for(int i = 0; i< result.getJSONObject("feed").getJSONArray("entry").length(); i++){
                    ENTRIES[i]=new ListEntry(result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("title").getString("$t"), result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("media$group").getJSONObject("yt$videoid").getString("$t"), false);
                }

                videoAdapter = new ArrayAdapter<ListEntry>(PlayerControlsDemoActivity.this, android.R.layout.simple_spinner_item, ENTRIES);
                videoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                videoChooser.setOnItemSelectedListener(PlayerControlsDemoActivity.this);
                videoChooser.setAdapter(videoAdapter);
                currentlySelectedPosition=0;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private String readResponse(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int len = 0;
        while ((len = is.read(data, 0, data.length)) >= 0) {
            bos.write(data, 0, len);
        }
        return new String(bos.toByteArray(), "UTF-8");
    }
}
