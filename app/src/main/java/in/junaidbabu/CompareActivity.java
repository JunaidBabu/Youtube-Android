package in.junaidbabu;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
    String url="https://r1---sn-nvoxu-ioql.googlevideo.com/videoplayback?id=o-AJTrcXTNtHolZekbXnsV_fRP5qBOFPdylN_-JkMgizRA&expire=1403690400&source=youtube&ms=au&mt=1403667628&fexp=900233%2C924222%2C927625%2C930008%2C931962%2C934030%2C937427%2C948200&mv=m&ipbits=0&ratebypass=yes&pm_type=static&pfa=5s&ip=182.171.224.146&key=yt5&gcr=jp&requiressl=yes&sparams=gcr%2Cid%2Cip%2Cipbits%2Citag%2Cpbr%2Cpfa%2Cpm_type%2Cratebypass%2Crequiressl%2Csource%2Cupn%2Cexpire&pbr=yes&sver=3&upn=VqTuAo-PQcU&mws=yes&itag=22&signature=8F42CD2DEFA515EB2D468575DFD4A13EFEFA64DC.4A0BC41821B17C5A74B2EC6D5E38761B4129C51C";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        t1=(TextView)findViewById(R.id.textView2);
        t2=(TextView)findViewById(R.id.textView3);
        // YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        //youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
        video= (CustomVideoView) findViewById(R.id.videoView);
        YouTubePlayerView videoview = ((YouTubePlayerView) findViewById(R.id.youtube_view));
        videoview.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider1, YouTubePlayer player1, boolean restored1) {
                player = player1;
                if (!restored1) {
                    video.setVideoURI(Uri.parse(url));

                    player.cueVideo("k4V3Mo61fJM");
                    player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {
                            Log.e("asfasdfasdf","loading");

                        }

                        @Override
                        public void onLoaded(String s) {
                            //player.play();
                            Log.e("asfasdfasdf","loaded");
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
     //   video.setVideoURI(Uri.parse(""));
        video.setVideoURI(Uri.parse(url));

        player.cueVideo("k4V3Mo61fJM");
        //player.play();
        ///video.start();
    }
}

