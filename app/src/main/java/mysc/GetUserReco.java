package mysc;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by babu on 12/6/14.
 */
public class GetUserReco extends AsyncTask<String, String, JSONObject> {

    AsyncResult asyncResult;

    public GetUserReco(AsyncResult as, String url) {
        asyncResult=as;
        this.execute(url);
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        URL url = null;
        JSONObject data = null;
        try {
            data = new JSONObject("{'knockkock': 'null here'}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            int sc = con.getResponseCode();
            if (sc == 200) {
                InputStream is = con.getInputStream();
                String json = readResponse(is);
                is.close();
                data = new JSONObject(json);
                //username.setText("Logged in as " + profileData.getJSONObject("entry").getJSONObject("title").getString("$t"));

            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return data;
    }
    @Override
    protected void onPostExecute(JSONObject result) {
      //  Log.i("log from the dark", result.toString());
        asyncResult.gotResult(result);
//        //Log.i("result", result);
//        try {
//            Log.e("Video Title", result.getJSONObject("feed").getJSONArray("entry").getJSONObject(0).getJSONObject("title").getString("$t"));
//            Log.e("Video id", result.getJSONObject("feed").getJSONArray("entry").getJSONObject(0).getJSONObject("media$group").getJSONObject("yt$videoid").getString("$t"));
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    private String readResponse(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int len = 0;
        while ((len = is.read(data, 0, data.length)) >= 0) {
            bos.write(data, 0, len);
        }
        return new String(bos.toByteArray(), "UTF-8");
    }

    public static abstract class AsyncResult{
        public abstract void gotResult(JSONObject s);
        //public abstract void somefunction(String a);
    }
}