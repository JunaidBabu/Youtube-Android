package in.junaidbabu;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class CompareActivity extends YouTubeBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerview_demo);

        // YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        //youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);



        ((YouTubePlayerView) findViewById(R.id.youtube_view)).initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
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

        ((YouTubePlayerView) findViewById(R.id.youtube_view2)).initialize("AIzaSyBmb9Yqu9vEBXjNOrBmZZ__6s12RH5RSv0", new YouTubePlayer.OnInitializedListener() {

            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean restored) {

                if (!restored) {
                    player.loadVideo("ctQAPiojDKE");
                }
            }


            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
            }
        });
    }



}
