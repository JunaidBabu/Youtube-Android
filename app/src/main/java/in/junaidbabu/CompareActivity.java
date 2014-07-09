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
    TextView t1,t2, LT;
    String url="https://r1---sn-nvoxu-ioql.googlevideo.com/videoplayback?mv=m&sver=3&requiressl=yes&mt=1404887139&id=o-AKAQ_2k3nJl5HSuce_w0z947gT7fIQMZ1livnDohbstp&upn=Ieb-vnPsYx8&expire=1404910800&ipbits=0&ratebypass=yes&gcr=jp&ms=au&mws=yes&itag=22&key=yt5&source=youtube&sparams=gcr%2Cid%2Cip%2Cipbits%2Citag%2Cratebypass%2Crequiressl%2Csource%2Cupn%2Cexpire&ip=182.171.224.146&fexp=902408%2C924213%2C924217%2C924222%2C930008%2C934024%2C934030&signature=8966A8106CD388F8BDA01F2D7C09261C98BFA2EF.DCCCF8514455D942DCE3B62DD443EC7B56B223C3";
    String vid="k4V3Mo61fJM";
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




    }

    public void seek(View v){
        EditText et = (EditText)findViewById(R.id.editText);

        player.seekToMillis(Integer.parseInt(et.getText().toString()));
        video.seekTo(Integer.parseInt(et.getText().toString()));
    }

    public void stop(View v){
        t1.setText(String.valueOf((float)player.getCurrentTimeMillis()/1000));
        t2.setText(String.valueOf((float) video.getCurrentPosition() / 1000));
        LT.setText(String.valueOf((float) video.getCurrentPosition() / 1000 - (float)player.getCurrentTimeMillis()/1000));
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

