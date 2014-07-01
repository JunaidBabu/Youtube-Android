package in .junaidbabu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.Toast;

import java.util.List;

import mysc.CustomVideoView;


public class MainVideoActivity extends Activity {
    private static String ACCESS_TOKEN = null;
    public static int NowPlaying, NowSelected;
    public static List<VideoClass> VC;
    public static List<List<VideoClass>> VCC;
    static String url_Playlist = "https://gdata.youtube.com/feeds/api/playlists/PL48A83AD3506C9D36?v=2&alt=json";
    private String url_recommendations = "https://gdata.youtube.com/feeds/api/users/default/recommendations?v=2&key="+DeveloperKey.DEVELOPER_KEY+"&alt=json&access_token=";
    public static CustomVideoView mVideoView;
    static Context c;
    int CacheCount;
    WebView mWebView;
    private MediaController mController;
    //MediaMetadataRetriever mMetadataRetriever;
    @ Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);
        getActionBar().hide();
        SharedPreferences status = getSharedPreferences("TOKEN", 0);
        ACCESS_TOKEN = status.getString("token", null);
        c = MainVideoActivity.this;
        mVideoView = (CustomVideoView) findViewById(R.id.myplaysurface);
        mVideoView.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
               // Toast.makeText(c, "Playing", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPause() {
                startActivity(new Intent(c, PlaylistActivity.class));
                Toast.makeText(c, "Paused", Toast.LENGTH_SHORT).show();
                //showDialog();
                //startActivity(new Intent(c, PlaylistActivity.class));
            }
        });

        //mMetadataRetriever = new MediaMetadataRetriever();
        mController = new MediaController(c, false);
        mVideoView.setMediaController(mController);
       // showDialog();
        startActivity(new Intent(c, PlaylistActivity.class));


    }


    @ Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
//    public void startPlayback(List<VideoClass> vc, final int pos) {
//        mController = new MediaController(c, false);
//        mVideoView.setMediaController(mController);
//        mVideoView.requestFocus();
//        NowPlaying = pos;
//        Toast(VCC.get(NowSelected).get(NowPlaying).getTitle());
//        if(vc.get(pos).getLongUrl()==null) {
//            GetUserReco.AsyncResult url = new GetUserReco.AsyncResult() {
//                @Override
//                public void gotResult(String s) {
//                    Toast(s);
//                    mVideoView.setVideoURI(Uri.parse(s));
//                    VCC.get(NowSelected).get(pos).setLongUrl(s);
//                    mVideoView.start();
//                }
//            };
//            new GetUserReco(url, getString(R.string.serverip) + vc.get(pos).getVid());
//            Log.w("url", getString(R.string.serverip) + vc.get(pos).getVid());
//        }else{
//            mVideoView.setVideoURI(Uri.parse(vc.get(pos).getLongUrl()));
//            mVideoView.start();
//        }
//
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //Toast(keyCode+"");
        switch (keyCode) {
            case 82: // Menu key
            {
                startActivity(new Intent(c, PlaylistActivity.class));

                return true;
            }
            case 22:
            {
                //Next
                PlaylistActivity.startPlayback(MainVideoActivity.VCC.get(MainVideoActivity.NowSelected), MainVideoActivity.NowPlaying + 1);
                return true;
            }
            case 21:
            {
                PlaylistActivity.startPlayback(MainVideoActivity.VCC.get(MainVideoActivity.NowSelected), MainVideoActivity.NowPlaying - 1);
                return true;
                //Prev
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void Toast(String s){
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
    }

}