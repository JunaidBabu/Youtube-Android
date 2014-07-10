package in.junaidbabu;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
    TextView t1,t2, LT;
    String url="https://r2---sn-nvoxu-ioqz.googlevideo.com/videoplayback?signature=C65E5457FC2BC450EAABC1739D4A1CF316797D64.1A75D961E3A7A06AE9BBB2C7FA6182B17FF62107&key=yt5&id=o-AAIWEYxKGhboDcIWWe1m9VuqSvu2bdWtbZvEwo1gJ1PS&fexp=900225%2C902408%2C902411%2C924213%2C924217%2C924222%2C930008%2C934024%2C934030%2C936927%2C939951%2C940647&ms=au&ipbits=0&mws=yes&mv=m&ratebypass=yes&sver=3&requiressl=yes&sparams=gcr%2Cid%2Cip%2Cipbits%2Citag%2Cratebypass%2Crequiressl%2Csource%2Cupn%2Cexpire&ip=182.171.224.146&upn=nOeXucesPws&itag=18&expire=1405000800&mt=1404976101&gcr=jp&source=youtube";
    String vid="uh8y-K-xC-E";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        t1=(TextView)findViewById(R.id.textView2);
        t2=(TextView)findViewById(R.id.textView3);
        LT=(TextView)findViewById(R.id.textView4);

        // YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        //youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
        video= (CustomVideoView) findViewById(R.id.videoView);

        MediaController mediaController = new MediaController(this, false);
        mediaController.setAnchorView(findViewById(R.id.videoView));
        video.setMediaController(mediaController);
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

        final Handler handler = new Handler();


//        handler.post( new Runnable(){
//
//            public void run() {
//                while (1==1) {
//                    try {
//                        sleep(500);
//                        t1.setText(String.valueOf((float) player.getCurrentTimeMillis() / 1000));
//                        t2.setText(String.valueOf((float) video.getCurrentPosition() / 1000));
//                        LT.setText(String.valueOf((float) video.getCurrentPosition() / 1000 - (float) player.getCurrentTimeMillis() / 1000));
//                    } catch (Exception e) {
//                    }
//                }
//            }
//        });

    }

    public void seek(View v){
        EditText et = (EditText)findViewById(R.id.editText);

        player.seekToMillis(Integer.parseInt(et.getText().toString()));
        video.seekTo(Integer.parseInt(et.getText().toString()));
    }

    public void stop(View v){
        t1.setText(String.valueOf((float)player.getCurrentTimeMillis()/1000));
        t2.setText(String.valueOf((float) video.getCurrentPosition() / 1000));
        LT.setText("Difference: "+ String.format("%.2f", (float) video.getCurrentPosition() / 1000 - (float)player.getCurrentTimeMillis()/1000)+" seconds");
        player.pause();
        video.pause();

    }
    public void reset(View v){
        video.seekTo(0);
        video.stopPlayback();
        video.setVideoURI(Uri.parse(""));
        video.setVideoURI(Uri.parse(url));

        player.cueVideo(vid);
        //player.play();
        video.start();


    }
}

