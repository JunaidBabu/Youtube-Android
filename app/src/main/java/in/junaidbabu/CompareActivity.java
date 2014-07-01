package in.junaidbabu;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import mysc.CustomVideoView;

public class CompareActivity extends YouTubeBaseActivity {
    YouTubePlayer player;
    CustomVideoView video;
    TextView t1,t2;
    String url="https://r1---sn-nvoxu-ioql.googlevideo.com/videoplayback?sver=3&mv=m&requiressl=yes&ipbits=0&itag=22&ip=182.171.224.146&sparams=gcr%2Cid%2Cip%2Cipbits%2Citag%2Cratebypass%2Crequiressl%2Csource%2Cupn%2Cexpire&fexp=902408%2C902531%2C914088%2C916612%2C917000%2C924222%2C930008%2C931014%2C934024%2C934030%2C936209%2C937427&upn=4YSB4Ua236Y&expire=1404198000&ms=au&id=o-ACa-e0QzVSL5yXk8KkpXzn6VOCLX_1rdR-Kg36XTupBy&mws=yes&key=yt5&mt=1404174281&gcr=jp&ratebypass=yes&source=youtube&signature=728C32076F3487B99F1E645149ACC36EAB1904.73B700F8B5F14A043B53EA5D139B6706F355BEC4";
    String vid="k4V3Mo61fJM";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        t1=(TextView)findViewById(R.id.textView2);
        t2=(TextView)findViewById(R.id.textView3);
        // YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        //youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
        video= (CustomVideoView) findViewById(R.id.videoView);


        video.setMediaController(new MediaController(this, false));
        YouTubePlayerView videoview = ((YouTubePlayerView) findViewById(R.id.youtube_view));
        videoview.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider1, YouTubePlayer player1, boolean restored1) {
                player = player1;
                if (!restored1) {
                    video.setVideoURI(Uri.parse(url));

                    player.cueVideo(vid);
                    player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {
                            Log.e("asfasdfasdf","loading");

                        }

                        @Override
                        public void onLoaded(String s) {
                            player.play();
                            Log.e("asfasdfasdf", "loaded");
                        }

                        @Override
                        public void onAdStarted() {
                            Log.e("asfasdfasdf","ad started");

                        }

                        @Override
                        public void onVideoStarted() {
                            Log.e("asfasdfasdf","video started");

                        }

                        @Override
                        public void onVideoEnded() {
                            Log.e("asfasdfasdf","ended");

                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {
                            Log.e("asfasdfasdf","asdfasdfasdf");

                        }
                    });

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider1, YouTubeInitializationResult result1) {
            }
        });




    }

    public void start(View v){
        //Toast.makeText(this, "Starting videos", Toast.LENGTH_SHORT).show();
        player.play();
        video.start();




    }

    public void seek(View v){
        EditText et = (EditText)findViewById(R.id.editText);

        player.seekToMillis(Integer.parseInt(et.getText().toString()));
        video.seekTo(Integer.parseInt(et.getText().toString()));
    }

    public void stop(View v){
        t1.setText(String.valueOf((float)player.getCurrentTimeMillis()/1000));
        t2.setText(String.valueOf((float) video.getCurrentPosition() / 1000));
        player.pause();
        video.pause();

    }
    public void reset(View v){
        video.stopPlayback();
        video.setVideoURI(Uri.parse(""));
        video.setVideoURI(Uri.parse(url));

        player.cueVideo(vid);
        //player.play();
        video.start();
    }
}

