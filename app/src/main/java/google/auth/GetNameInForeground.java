package google.auth;


import java.io.IOException;


import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import in.junaidbabu.MainActivity;

/**
 * This example shows how to fetch tokens if you are creating a foreground task/activity and handle
 * auth exceptions.
 */
public class GetNameInForeground extends AbstractGetNameTask {

  public GetNameInForeground(MainActivity activity, String email, String scope) {
      super(activity, email, scope);
  }

  /**
   * Get a authentication token if one is not available. If the error is not recoverable then
   * it displays the error message on parent activity right away.
   */
  @Override
  protected String fetchToken() throws IOException {
      try {
          String token = GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
          Log.i("TOKEN", token);
          return token;
      } catch (GooglePlayServicesAvailabilityException playEx) {
          // GooglePlayServices.apk is either old, disabled, or not present.
      } catch (UserRecoverableAuthException userRecoverableException) {
          // Unable to authenticate, but the user can fix this.
          // Forward the user to the appropriate activity.
          Log.e("Exception", "UserRecoverableAuthException");
          mActivity.startActivityForResult(userRecoverableException.getIntent(), mRequestCode);
      } catch (GoogleAuthException fatalException) {
          onError("Unrecoverable error " + fatalException.getMessage(), fatalException);
      }
      return null;
  }
}