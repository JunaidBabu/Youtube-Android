package mysc;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by babu on 12/6/14.
 */
public class GetUserReco extends AsyncTask<String, String, String> {

    AsyncResult asyncResult;

    public GetUserReco(AsyncResult as, String url) {
        asyncResult=as;
        this.execute(url);
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        String json = "{'knockkock': 'null here'}";

        try {
            url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            int sc = con.getResponseCode();
            if (sc == 200) {
                InputStream is = con.getInputStream();
                json = readResponse(is);
                is.close();
            }
        } catch (Exception e) {
        }

        return json;
    }
    @Override
    protected void onPostExecute(String result) {
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
        public abstract void gotResult(String s);
        //public abstract void somefunction(String a);
    }
}