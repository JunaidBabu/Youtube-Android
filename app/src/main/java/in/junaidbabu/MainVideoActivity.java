package in .junaidbabu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mysc.CustomVideoView;
import mysc.GetUserReco;

public class MainVideoActivity extends Activity {
    private static String ACCESS_TOKEN = null;
    public List<VideoClass> VC;
    private String url_recommendations = "https://gdata.youtube.com/feeds/api/users/default/recommendations?v=2&key="+DeveloperKey.DEVELOPER_KEY+"&alt=json&access_token=";
    private CustomVideoView mVideoView;
    Context c;
    WebView mWebView;
    private MediaController mController;
    MediaMetadataRetriever mMetadataRetriever;
    @ Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);

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
                showDialog();
            }
        });
        mMetadataRetriever = new MediaMetadataRetriever();
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        showDialog();

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("https://www.youtube.com/watch?v=68AqHwgk2s8");
        //startActivityForResult(Intent.createChooser(intent, "Video File to Play"), 0);
    }
    public void showDialog(){

        GetUserReco.AsyncResult asyncResult = new GetUserReco.AsyncResult() {
            @Override
            public void gotResult(JSONObject result) {
                Log.i("json", result.toString());
                final List<String> your_array_list = new ArrayList<String>();
                String title, id;
                VC = new ArrayList<VideoClass>();
                try {
                    for(int i = 0; i< result.getJSONObject("feed").getJSONArray("entry").length(); i++){
                        title = (result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("title").getString("$t"));
                        id=result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("media$group").getJSONObject("yt$videoid").getString("$t");
                        VC.add(new VideoClass(id, title, ""));
                        your_array_list.add(result.getJSONObject("feed").getJSONArray("entry").getJSONObject(i).getJSONObject("title").getString("$t"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final Dialog dialog = new Dialog(c);
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("Playlist...");
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("List of videos to be populated");
                ListView lv = (ListView) dialog.findViewById(R.id.listView);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        c,
                        android.R.layout.simple_list_item_1,
                        your_array_list );

                lv.setAdapter(arrayAdapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast(VC.get(i).getVid());
                        mWebView.loadUrl("https://www.youtube.com/embed/"+VC.get(i).getVid());
                        dialog.dismiss();
                    }
                });
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.ic_launcher);
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Toast("toast test");
                                                        dialog.dismiss();
                                                    }
                                                }
                );
                dialog.show();
            }
        };
        new GetUserReco(asyncResult, url_recommendations+ACCESS_TOKEN);


    }
    @ Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    public void startPlayback(String videoPath) {
        mMetadataRetriever.setDataSource(videoPath);
        Uri uri = Uri.parse(videoPath);
        mVideoView.setVideoURI(uri);
        mController = new MediaController(this, false);
        mVideoView.setMediaController(mController);
        mVideoView.requestFocus();
        mVideoView.start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Uri sourceUri = data.getData();
                String source = getPath(sourceUri);
                startPlayback(source);
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