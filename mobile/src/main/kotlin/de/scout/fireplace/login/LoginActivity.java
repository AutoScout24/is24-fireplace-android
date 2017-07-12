package de.scout.fireplace.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.widget.FrameLayout;
import butterknife.BindView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import de.scout.fireplace.R;
import de.scout.fireplace.activity.AbstractActivity;
import de.scout.fireplace.home.HomeActivity;
import de.scout.fireplace.network.ErrorHandler;
import de.scout.fireplace.network.TokenRepository;
import javax.inject.Inject;

public class LoginActivity extends AbstractActivity {

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
      gotoHomeActivity();
      return;
    }

    if (availability.isUserResolvableError(status)) {
      availability.getErrorDialog(this, status, 1).show();
    } else {
      Snackbar.make(content, "Google Play Services unavailable", Snackbar.LENGTH_INDEFINITE).show();
    }
  }

  private void signInAnonymously() {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    if (firebaseAuth.getCurrentUser() == null) {
      FirebaseAuth.getInstance().signInAnonymously();
    }
  }

  private void gotoHomeActivity() {
    HomeActivity.start(LoginActivity.this);
    finish();
  }
}
