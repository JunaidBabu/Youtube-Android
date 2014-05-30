package in.junaidbabu;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import google.auth.AbstractGetNameTask;
import google.auth.GetNameInForeground;
import mysc.UndoBarController;


public class MainActivity extends ActionBarActivity implements UndoBarController.UndoListener{

    private static final int PICK_ACCOUNT_REQUEST = 21;
    private Context mContext = this;
    String Scope = "oauth2:https://gdata.youtube.com";

    private UndoBarController mUndoBarController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        mUndoBarController = new UndoBarController(findViewById(R.id.undobar), this);

        mUndoBarController.showUndoBar(
                false,
                "Isn't this awesome? :D",
                null);

        Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null,
                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null) ;
        startActivityForResult(googlePicker,PICK_ACCOUNT_REQUEST);
    }

    @Override
    public void onUndo(Parcelable token) {
        // Perform the undo
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUndoBarController.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mUndoBarController.onRestoreInstanceState(savedInstanceState);
    }
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        if (requestCode == PICK_ACCOUNT_REQUEST && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            //Toast.makeText(this,"Account Name="+accountName, 3000).show();
            if (isNetworkAvailable()) {
                getTask((MainActivity) mContext, accountName, Scope).execute();
            } else {
                Toast.makeText(mContext, "No Network Service!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    private AbstractGetNameTask getTask(MainActivity activity, String email,
                                        String scope) {
        return new GetNameInForeground(activity, email, scope);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            startActivity(new Intent(this, YouTube.class));
            startActivity(new Intent(this, PlayerControlsDemoActivity.class));
            return true;
        }else if (id == R.id.action_videoview){
            startActivity(new Intent(this, VideoViewActivity.class));
        }else if (id == R.id.action_videoview2){
            startActivity(new Intent(this, VideoViewActivity2.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e("Network Testing", "***Available***");
            return true;
        }
        Log.e("Network Testing", "***Not Available***");
        return false;
    }
}
