package in .junaidbabu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.Toast;

import java.util.List;

import mysc.CustomVideoView;
import mysc.GetUserReco;


public class MainVideoActivity extends Activity {
    private static String ACCESS_TOKEN = null;
    public static int NowPlaying;
    public static List<VideoClass> VC;
    static String url_Playlist = "https://gdata.youtube.com/feeds/api/playlists/PL48A83AD3506C9D36?v=2&alt=json";
    private String url_recommendations = "https://gdata.youtube.com/feeds/api/users/default/recommendations?v=2&key="+DeveloperKey.DEVELOPER_KEY+"&alt=json&access_token=";
    public static CustomVideoView mVideoView;
    static Context c;
    int CacheCount;
    WebView mWebView;
    private MediaController mController;
    MediaMetadataRetriever mMetadataRetriever;
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
                Toast.makeText(c, "playing", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPause() {
                Toast.makeText(c, "paused", Toast.LENGTH_SHORT).show();
                //showDialog();
                startActivity(new Intent(c, PlaylistActivity.class));
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast("Stop playback");
                startPlayback(VC, NowPlaying+1);
                //showDialog();
            }
        });
        mMetadataRetriever = new MediaMetadataRetriever();
        mController = new MediaController(c, false);
        mVideoView.setMediaController(mController);
       // showDialog();
        startActivity(new Intent(c, PlaylistActivity.class));


    }

//    public void showDialog(){
//        if(VC!=null){
//            //Toast("Somebody is there"+VC.toString());
//            final Dialog dialog = new Dialog(c);
//           // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.setContentView(R.layout.custom);
//            dialog.setTitle("Playlist...");
//            ListView lv = (ListView) dialog.findViewById(R.id.listView);
//            ListView lv2 = (ListView) dialog.findViewById(R.id.listView2);
//
//            CustomList adapter = new
//                    CustomList(getApplicationContext(), VC);
//            lv.setAdapter(adapter);
//            lv2.setAdapter(adapter);
//
//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
//                    Toast(VC.get(i).getVid());
//                    startPlayback(VC, i);
//
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
//            return;
//        }
//        GetUserReco.AsyncResult asyncResult = new GetUserReco.AsyncResult() {
//            @Override
//            public void gotResult(String s) {
//                JSONObject result= null;
//                try {
//                    result = new JSONObject(s);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Log.i("json", result.toString());
//                //final List<String> your_array_list = new ArrayList<String>();
//                String title, id, image;
//                VC = new ArrayList<VideoClass>();
//                try {
//                    for(int i = 0; i< result.getJSONObject("feed").getJSONArray("entry").length(); i++){
//                        title = (result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("title").getString("$t"));
//                        id=result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("media$group").getJSONObject("yt$videoid").getString("$t");
//                        //image=result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("media$group").getJSONArray("media$thumbnail").getJSONObject(3).getString("url");
//                        VC.add(new VideoClass(id, title, "http://img.youtube.com/vi/" + id + "/default.jpg"));
//                        //your_array_list.add(result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("title").getString("$t"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                final Dialog dialog = new Dialog(c);
//                dialog.setContentView(R.layout.custom);
//                dialog.setTitle("Playlist...");
//               // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                ListView lv = (ListView) dialog.findViewById(R.id.listView);
//                ListView lv2 = (ListView) dialog.findViewById(R.id.listView2);
//
//                CustomList adapter = new
//                        CustomList(getApplicationContext(), VC);
//                lv.setAdapter(adapter);
//                lv2.setAdapter(adapter);
//
//                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
//                        Toast(VC.get(i).getVid());
//                        startPlayback(VC, i);
//
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//
//
//                for (CacheCount=0; CacheCount<VC.size(); CacheCount++){
//                    //new GetUserReco(url, "http://192.168.11.72:5000/" + VC.get(CacheCount).getVid());
//                    VC.get(CacheCount).parseLongUrl();
//                }
//            }
//        };
////        new GetUserReco(asyncResult, url_recommendations+ACCESS_TOKEN);
//        new GetUserReco(asyncResult, url_Playlist);
//
//
//
//    }
    @ Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    public void startPlayback(List<VideoClass> vc, final int pos) {
        mController = new MediaController(c, false);
        mVideoView.setMediaController(mController);
        mVideoView.requestFocus();
        NowPlaying = pos;
        if(vc.get(pos).getLongUrl()==null) {
            GetUserReco.AsyncResult url = new GetUserReco.AsyncResult() {
                @Override
                public void gotResult(String s) {
                    Toast(s);
                    mVideoView.setVideoURI(Uri.parse(s));
                    VC.get(pos).setLongUrl(s);
                    mVideoView.start();
                }
            };
            new GetUserReco(url, getString(R.string.serverip) + vc.get(pos).getVid());
            Log.w("url", getString(R.string.serverip) + vc.get(pos).getVid());
        }else{
            mVideoView.setVideoURI(Uri.parse(vc.get(pos).getLongUrl()));
            mVideoView.start();
        }
        //mMetadataRetriever.setDataSource(videoPath);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Uri sourceUri = data.getData();
                String source = getPath(sourceUri);
                //startPlayback(source);
            }
        }
    }
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        }
        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    public void Toast(String s){
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
    }

}