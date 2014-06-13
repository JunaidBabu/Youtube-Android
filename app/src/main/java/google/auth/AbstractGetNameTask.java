package google.auth;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import in.junaidbabu.MainActivity;
import in.junaidbabu.MainVideoActivity;

/**
 * Display personalized greeting. This class contains boilerplate code to
 * consume the token but isn't integral to getting the tokens.
 */
public abstract class AbstractGetNameTask extends AsyncTask<Void, Void, Void> {
	@SuppressWarnings("unused")
	private static final String TAG = "TokenInfoTask";
	protected MainActivity mActivity;
   public static String GOOGLE_USER_DATA="No_data";
	protected String mScope;
	protected String mEmail;
	protected int mRequestCode;
	String Tmsg=null;
	
	protected AbstractGetNameTask(MainActivity activity, String email, String scope) {
		this.mActivity = activity;
		this.mScope = scope;
		this.mEmail = email;
	}
	@Override
	protected void onPostExecute(Void a) {

    }
	@Override
	protected Void doInBackground(Void... params) {
		
		try {
			fetchNameFromProfileServer();

		} catch (IOException ex) {
			onError("Following Error occured, please try again. "
					+ ex.getMessage(), ex);
		} catch (JSONException e) {
			onError("Bad response: " + e.getMessage(), e);
		}
		return null;
	}

	protected void onError(String msg, Exception e) {
		//if (e != null) {
			Tmsg = msg;
			Log.w("Exception: ", msg);
			//Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
		//}
			
	}

	/**
	 * Get a authentication token if one is not available. If the error is not
	 * recoverable then it displays the error message on parent activity.
	 */
	protected abstract String fetchToken() throws IOException;

	/**
	 * Contacts the user info server to get the profile of the user and extracts
	 * the first name of the user from the profile. In order to authenticate
	 * with the user info server the method first fetches an access token from
	 * Google Play services.
	 * @return 
	 * @return 
	 * 
	 * @throws IOException
	 *             if communication with user info server failed.
	 * @throws JSONException
	 *             if the response from the server could not be parsed.
	 */
	private void fetchNameFromProfileServer() throws IOException, JSONException {
		String token = fetchToken();
       // TextView textView = (TextView) mActivity.findViewById(R.id.t1);
        //textView.setText(token);
        try{
            Log.e("Token saving to shared pref", token);
            SharedPreferences example = mActivity.getSharedPreferences("TOKEN", 0);
            SharedPreferences.Editor editor = example.edit();
            editor.putString("token", token);
            editor.commit();
            mActivity.startActivity(new Intent(mActivity, MainVideoActivity.class));
            mActivity.finish();

        }catch (Exception e){
          Log.e("Exception", "Expected");
           // e.printStackTrace();
        }
	}


	

	/**
	 * Reads the response from the input stream and returns it as a string.
	 */
	private static String readResponse(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] data = new byte[2048];
		int len = 0;
		while ((len = is.read(data, 0, data.length)) >= 0) {
			bos.write(data, 0, len);
		}
		return new String(bos.toByteArray(), "UTF-8");
	}

}