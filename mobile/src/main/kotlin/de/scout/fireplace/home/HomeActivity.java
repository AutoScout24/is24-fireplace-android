package de.scout.fireplace.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import de.scout.fireplace.R;
import de.scout.fireplace.activity.AbstractActivity;
import de.scout.fireplace.models.Expose;
import de.scout.fireplace.network.ErrorHandler;
import de.scout.fireplace.network.SchedulingStrategy;
import de.scout.fireplace.preference.PreferenceActivity;
import de.scout.fireplace.search.SearchClient;
import de.scout.fireplace.ui.FloatingCardStackLayout;
import de.scout.fireplace.ui.FloatingCardView;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class HomeActivity extends AbstractActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  private static final float SCOUT_LATITUDE = 52.512500F;
  private static final float SCOUT_LONGITUDE = 13.431020F;

  private static final int CARD_RELOAD_SIZE = 2;
  private static final int CARD_PAGE_SIZE = 4;

  private static final String WIDTH_TAG = "%WIDTH%";
  private static final String HEIGHT_TAG = "%HEIGHT%";

  private CompositeDisposable disposables = new CompositeDisposable();

  private int page = 1;
  private int max = 1; // todo check against max page size and show dialog

  private GoogleApiClient google;

  @BindView(R.id.coordinator) CoordinatorLayout coordinator;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.avatar) ImageView avatar;
  @BindView(R.id.title) TextView title;

  @BindView(R.id.stack) FloatingCardStackLayout stack;

  @BindView(R.id.action_pass) ImageButton pass;
  @BindView(R.id.action_like) ImageButton like;

  @Inject SearchClient client;
  @Inject EventMatcher matcher;

  @Inject ErrorHandler handler;
  @Inject SchedulingStrategy strategy;

  public static void start(Context context) {
    context.startActivity(new Intent(context, HomeActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(toolbar);
    setUpSupportActionBar(getSupportActionBar());
    setUpActionButtons();

    setUpGoogleApiClient();
    setUpPipeline();
  }

  private void setUpGoogleApiClient() {
    google = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }

  @Override
  protected void onStart() {
    super.onStart();
    google.connect();
  }

  @Override
  protected void onStop() {
    super.onStop();
    google.disconnect();
  }

  private void setUpPipeline() {
    Disposable disposable = stack.events()
        .doOnNext(event -> {
          if (matcher.match(event)) {
            onMatch(event.getSummary());
          }
        })
        .filter(event -> event.getCount() <= CARD_RELOAD_SIZE)
        .subscribe(integer -> {
          if (!google.isConnected()) {
            return;
          }

          onConnected(null);
        });

    disposables.add(disposable);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    disposables.dispose();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_home;
  }

  private void setUpSupportActionBar(ActionBar actionBar) {
    actionBar.setDisplayShowTitleEnabled(false);
  }

  private void setUpActionButtons() {
    ViewCompat.setElevation(pass, getResources().getDimension(R.dimen.default_elevation));
    ViewCompat.setElevation(like, getResources().getDimension(R.dimen.default_elevation));
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      onError(R.string.error_permissions);
      return;
    }

    Location location = check(LocationServices.FusedLocationApi.getLastLocation(google));
    if (location == null) {
      return; // TODO: 4/21/17 Request location updates
    }

    fetch(location);
  }

  private Location check(Location location) {
    if (location.getLatitude() == 0 && location.getLongitude() == 0) {
      title.setTextColor(ContextCompat.getColor(this, R.color.light_blue));

      location.setLatitude(SCOUT_LATITUDE);
      location.setLongitude(SCOUT_LONGITUDE);
    } else {
      title.setTextColor(ContextCompat.getColor(this, android.R.color.black));
    }

    return location;
  }

  @Override
  public void onConnectionSuspended(int i) {
    onError(R.string.connection_suspended_message);
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult result) {
    onError(R.string.connection_failed_message);
  }

  private void onError(@StringRes int resId) {
    new AlertDialog.Builder(this)
        .setTitle(R.string.error_dialog_title)
        .setMessage(resId)
        .show();
  }

  private void fetch(Location location) {
    Disposable disposable = client.search(location, page++, CARD_PAGE_SIZE)
        .doOnSuccess(search -> max = search.getNumberOfPages())
        .flatMapObservable(search -> Observable.fromIterable(search.getResults()))
        .map(item -> {

          // TODO: 7/11/17 Extract transformation method to data class
          String pictureUrl = item.getPictureUrl();
          if (pictureUrl != null) {
            pictureUrl = pictureUrl.replace(WIDTH_TAG, String.valueOf(stack.getWidth()));
            pictureUrl = pictureUrl.replace(HEIGHT_TAG, String.valueOf(stack.getHeight()));
          }

          return new Expose.Summary(item.getId(), item.getInfoLine(), TextUtils.join(" | ", item.getAttributes()), pictureUrl);
        })
        .subscribe(summary -> {
          FloatingCardView card = new FloatingCardView(HomeActivity.this);
          card.bind(summary);
          stack.addCard(card);
        }, handler);

    disposables.add(disposable);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu_home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_preferences:
        PreferenceActivity.start(this);
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @OnClick(R.id.action_pass)
  void onPassClick() {
    stack.getTopChild().dismiss();
  }

  @OnClick(R.id.action_like)
  void onLikeClick() {
    stack.getTopChild().approve();
  }

  private void onMatch(Expose.Summary summary) {
    MatchFragment fragment = new MatchFragment();
    fragment.bind(summary);

    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.coordinator, fragment)
        .commit();
  }
}
