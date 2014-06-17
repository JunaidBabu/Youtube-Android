package in.junaidbabu;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mysc.CustomList;
import mysc.GetUserReco;

public class PlaylistActivity extends Activity {

    static String url_Playlist2 = "https://gdata.youtube.com/feeds/api/playlists/PL55713C70BA91BD6E?v=2&alt=json";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlistactivity);
        context=this;
        new GetUserReco(new GetUserReco.AsyncResult() {
            @Override
            public void gotResult(String s) {
                ListView lv = (ListView) findViewById(R.id.listView);
                ListPop(lv, s);
            }
        }, MainVideoActivity.url_Playlist);


        new GetUserReco(new GetUserReco.AsyncResult() {
            @Override
            public void gotResult(String s) {
                ListView lv = (ListView) findViewById(R.id.listView2);
                ListPop(lv, s);
            }
        }, MainVideoActivity.url_Playlist);


        new GetUserReco(new GetUserReco.AsyncResult() {
            @Override
            public void gotResult(String s) {
                ListView lv = (ListView) findViewById(R.id.listView3);
                ListPop(lv, s);
            }
        }, url_Playlist2);


        new GetUserReco(new GetUserReco.AsyncResult() {
            @Override
            public void gotResult(String s) {
                ListView lv = (ListView) findViewById(R.id.listView4);
                ListPop(lv, s);
            }
        }, MainVideoActivity.url_Playlist);

    }


    public void ListPop(ListView v, String s){
        JSONObject result= null;
        try {
            result = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("json", result.toString());
        String title, id;
        MainVideoActivity.VC = new ArrayList<VideoClass>();
        try {
            for(int i = 0; i< result.getJSONObject("feed").getJSONArray("entry").length(); i++){
                title = (result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("title").getString("$t"));
                id=result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("media$group").getJSONObject("yt$videoid").getString("$t");
                MainVideoActivity.VC.add(new VideoClass(id, title, "http://img.youtube.com/vi/" + id + "/default.jpg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // v = (ListView) findViewById(R.id.listView);

        CustomList adapter = new
                CustomList(getApplicationContext(), MainVideoActivity.VC);
        v.setAdapter(adapter);

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                startPlayback(MainVideoActivity.VC, i);
                finish();
            }
        });
    }

    public void startPlayback(List<VideoClass> vc, final int pos) {

        MainVideoActivity.mVideoView.requestFocus();
        MainVideoActivity.NowPlaying = pos;
        if(vc.get(pos).getLongUrl()==null) {
            GetUserReco.AsyncResult url = new GetUserReco.AsyncResult() {
                @Override
                public void gotResult(String s) {

                    MainVideoActivity.mVideoView.setVideoURI(Uri.parse(s));
                    MainVideoActivity.VC.get(pos).setLongUrl(s);
                    MainVideoActivity.mVideoView.start();
                }
            };
            new GetUserReco(url, getString(R.string.serverip) + vc.get(pos).getVid());
            Log.w("url", getString(R.string.serverip) + vc.get(pos).getVid());
        }else{
            MainVideoActivity.mVideoView.setVideoURI(Uri.parse(vc.get(pos).getLongUrl()));
            MainVideoActivity.mVideoView.start();
        }
        //mMetadataRetriever.setDataSource(videoPath);

    }
}
