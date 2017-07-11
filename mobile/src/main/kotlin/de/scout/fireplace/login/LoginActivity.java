package de.scout.fireplace.login;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.FrameLayout;
import butterknife.BindView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import de.scout.fireplace.R;
import de.scout.fireplace.activity.AbstractActivity;
import de.scout.fireplace.home.HomeActivity;
import de.scout.fireplace.network.ErrorHandler;
import de.scout.fireplace.network.TokenRepository;
import javax.inject.Inject;

public class LoginActivity extends AbstractActivity {

  private static final int PERMISSION_REQUEST_CODE = 0x14;

  private static final String CLIENT_ID = "AndroidApp-QuickCheckKey";
  private static final String CLIENT_SECRET = "1Wg0YZtnQQZCntxN";

  @BindView(R.id.content) FrameLayout content;

  @Inject LoginClient client;
  @Inject ErrorHandler handler;
  @Inject TokenRepository repository;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    client.clientCredentials(CLIENT_ID, CLIENT_SECRET)
        .doOnSuccess(token -> repository.accessToken(token.getAccessToken()))
        .subscribe(token -> {
          checkPlayServices();
          signInAnonymously();
        }, handler);
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_login;
  }

  private void checkPlayServices() {
    GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
    int status = availability.isGooglePlayServicesAvailable(this);

    if (status == ConnectionResult.SUCCESS) {
      signInAnonymously();
      checkSelfPermissions();
      return;
    }

    if (availability.isUserResolvableError(status)) {
      availability.getErrorDialog(this, status, 1).show();
    } else {
      Snackbar.make(content, "Google Play Services unavailable", Snackbar.LENGTH_INDEFINITE).show();
    }
  }

  private void signInAnonymously() {
    /*FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    if (firebaseAuth.getCurrentUser() == null) {
      FirebaseAuth.getInstance().signInAnonymously();
    }*/
  }

  private void checkSelfPermissions() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
      showRequestPermissionRationale();
      return;
    }

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      requestPermissions();
      return;
    }

    gotoHomeActivity();
  }

  private void requestPermissions() {
    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_REQUEST_CODE);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (permissions.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
      checkSelfPermissions();
      return;
    }

    gotoHomeActivity();
  }

  private void showRequestPermissionRationale() {
    new AlertDialog.Builder(this)
        .setTitle(R.string.permission_dialog_title)
        .setMessage(R.string.permission_dialog_message)
        .setPositiveButton(android.R.string.ok, (dialog, which) -> requestPermissions())
        .show();
  }

  private void gotoHomeActivity() {
    HomeActivity.start(LoginActivity.this);
    finish();
  }
}
