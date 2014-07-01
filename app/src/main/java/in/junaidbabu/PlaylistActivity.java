package in.junaidbabu;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mysc.CustomList;
import mysc.GetUserReco;

public class PlaylistActivity extends Activity {

    static String url_PlaylistBill14 = "https://gdata.youtube.com/feeds/api/playlists/PL55713C70BA91BD6E?v=2&alt=json";
    static String url_PlaylistBill13 = "https://gdata.youtube.com/feeds/api/playlists/PL1C8AD08E6C3AE087?v=2&alt=json";
    static String url_PlaylistRock = "https://gdata.youtube.com/feeds/api/playlists/PL0420B28C0AB16AD6?v=2&alt=json";


    static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlistactivity);
        context=this;


        try {
            if (MainVideoActivity.VCC.get(0).size() > 0) {

                manual((ListView) findViewById(R.id.listView), 0);
                manual((ListView) findViewById(R.id.listView2), 1);
                manual((ListView) findViewById(R.id.listView3), 2);
                manual((ListView) findViewById(R.id.listView4), 3);
                return;
            }
        }catch (Exception e){

        }

        MainVideoActivity.VCC = new ArrayList<List<VideoClass>>();
        MainVideoActivity.VCC.add(new ArrayList<VideoClass>());
        MainVideoActivity.VCC.add(new ArrayList<VideoClass>());
        MainVideoActivity.VCC.add(new ArrayList<VideoClass>());
        MainVideoActivity.VCC.add(new ArrayList<VideoClass>());


        MainVideoActivity.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //Toast("Stop playback");
                startPlayback(MainVideoActivity.VCC.get(MainVideoActivity.NowSelected), MainVideoActivity.NowPlaying + 1);
                //showDialog();
            }
        });



        new GetUserReco(new GetUserReco.AsyncResult() {
            @Override
            public void gotResult(String s) {
                ListView lv = (ListView) findViewById(R.id.listView);
                ListPop(lv, s, 0);
            }
        }, MainVideoActivity.url_Playlist);


        new GetUserReco(new GetUserReco.AsyncResult() {
            @Override
            public void gotResult(String s) {
                ListView lv = (ListView) findViewById(R.id.listView2);
                ListPop(lv, s, 1);
            }
        }, url_PlaylistRock);


        new GetUserReco(new GetUserReco.AsyncResult() {
            @Override
            public void gotResult(String s) {
                ListView lv = (ListView) findViewById(R.id.listView3);
                ListPop(lv, s, 2);
            }
        }, url_PlaylistBill13);


        new GetUserReco(new GetUserReco.AsyncResult() {
            @Override
            public void gotResult(String s) {
                ListView lv = (ListView) findViewById(R.id.listView4);
                ListPop(lv, s, 3);
            }
        }, url_PlaylistBill14);


    }


    public void ListPop(ListView v, String s, final int Pid){

        JSONObject result= null;
        try {
            result = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("json", result.toString());
        String title, id;


        try {
            for(int i = 0; i< result.getJSONObject("feed").getJSONArray("entry").length(); i++){
                title = (result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("title").getString("$t"));
                id=result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("media$group").getJSONObject("yt$videoid").getString("$t");
                MainVideoActivity.VCC.get(Pid).add(new VideoClass(context, id, title, "http://img.youtube.com/vi/" + id + "/default.jpg"));
                MainVideoActivity.VCC.get(Pid).get(i).parseLongUrl();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // v = (ListView) findViewById(R.id.listView);

        CustomList adapter = new
                CustomList(getApplicationContext(), MainVideoActivity.VCC.get(Pid));
        v.setAdapter(adapter);

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                MainVideoActivity.NowSelected=Pid;
                startPlayback(MainVideoActivity.VCC.get(Pid), i);
                finish();
            }
        });
    }
    public void manual(ListView v, final int Pid){
        CustomList adapter = new
                CustomList(getApplicationContext(), MainVideoActivity.VCC.get(Pid));
        v.setAdapter(adapter);

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                MainVideoActivity.NowSelected=Pid;
                startPlayback(MainVideoActivity.VCC.get(Pid), i);
                finish();
            }
        });
    }

    public static void startPlayback(List<VideoClass> vc, final int pos) {
        MainVideoActivity.mVideoView.requestFocus();
        MainVideoActivity.NowPlaying = pos;
        Toast(MainVideoActivity.VCC.get(MainVideoActivity.NowSelected).get(MainVideoActivity.NowPlaying).getTitle());

        if(vc.get(pos).getLongUrl()==null) {
            GetUserReco.AsyncResult url = new GetUserReco.AsyncResult() {
                @Override
                public void gotResult(String s) {

                    MainVideoActivity.mVideoView.setVideoURI(Uri.parse(s));
                    MainVideoActivity.VCC.get(MainVideoActivity.NowSelected).get(pos).setLongUrl(s);
                    MainVideoActivity.mVideoView.start();
                }
            };
            new GetUserReco(url, context.getString(R.string.serverip) + vc.get(pos).getVid());
            Log.w("url", context.getString(R.string.serverip) + vc.get(pos).getVid());
        }else{
            MainVideoActivity.mVideoView.setVideoURI(Uri.parse(vc.get(pos).getLongUrl()));
            MainVideoActivity.mVideoView.start();
        }
        //mMetadataRetriever.setDataSource(videoPath);

    }
    public static void Toast(String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
