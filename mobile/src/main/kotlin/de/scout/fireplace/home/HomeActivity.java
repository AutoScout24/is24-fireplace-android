package de.scout.fireplace.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import de.scout.fireplace.BuildConfig;
import de.scout.fireplace.R;
import de.scout.fireplace.activity.AbstractActivity;
import de.scout.fireplace.bus.RxBus;
import de.scout.fireplace.bus.events.TopCardClickedEvent;
import de.scout.fireplace.models.Expose;
import de.scout.fireplace.network.ErrorHandler;
import de.scout.fireplace.network.SchedulingStrategy;
import de.scout.fireplace.preference.PreferenceActivity;
import de.scout.fireplace.search.SearchClient;
import de.scout.fireplace.ui.FloatingCardStackEvent;
import de.scout.fireplace.ui.FloatingCardStackLayout;
import de.scout.fireplace.ui.FloatingCardView;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class HomeActivity extends AbstractActivity {

  private static final int CARD_RELOAD_SIZE = 2;
  private static final int CARD_PAGE_SIZE = 4;

  private static final int REQUEST_CODE_PERMISSION = 0x14;

  private static final float SCOUT_LATITUDE = 52.512500F;
  private static final float SCOUT_LONGITUDE = 13.431020F;

  private CompositeDisposable disposables;
  private FusedLocationProviderClient provider;

  private int page = 1;

  @BindView(R.id.coordinator) CoordinatorLayout coordinator;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.title) TextView title;

  @BindView(R.id.stack) FloatingCardStackLayout stack;

  @BindView(R.id.action_pass) ImageButton pass;
  @BindView(R.id.action_like) ImageButton like;

  @Inject SearchClient client;
  @Inject EventMatcher matcher;
  @Inject HomeReporting reporting;

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

    setUpLocationProvider();
    setUpDisposables();
    setUpPipeline();
  }

  private void setUpLocationProvider() {
    provider = LocationServices.getFusedLocationProviderClient(this);
  }

  private void setUpDisposables() {
    if (disposables != null) {
      disposables.dispose();
      disposables = null;
    }

    disposables = new CompositeDisposable();
  }

  private void setUpPipeline() {
    Disposable disposable = stack.events()
        .doOnNext(event -> {
          if (matcher.match(event)) {
            onMatch(event.getExpose());
          }
        })
        .doOnNext(event -> {
          if (event.getType().equals(FloatingCardStackEvent.Type.LIKE)) {
            reporting.reportLike(event.getExpose(), true);
          } else if (event.getType().equals(FloatingCardStackEvent.Type.PASS)) {
            reporting.reportPass(event.getExpose(), true);
          }
        })
        .filter(event -> event.getCount() - 1 <= CARD_RELOAD_SIZE)
        .subscribe(integer -> getLastLocation());

    RxBus.getInstance().toObserverable()
        .filter(event -> event instanceof TopCardClickedEvent)
        .subscribe(event -> onTopCardClicked(((TopCardClickedEvent) event).getExpose()));

    disposables.add(disposable);
  }

  @Override
  public void onStart() {
    super.onStart();

    if (!checkPermissions()) {
      requestPermissions();
    } else {
      getLastLocation();
    }
  }

  private boolean checkPermissions() {
    return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
  }

  private void requestPermissions() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
      showSnackbar(R.string.location_permission_rationale, android.R.string.ok, view -> startPermissionRequest());
    } else {
      startPermissionRequest();
    }
  }

  private void showSnackbar(@StringRes int resId) {
    Snackbar.make(coordinator, getString(resId), Snackbar.LENGTH_LONG).show();
  }

  private void showSnackbar(@StringRes int testResId, @StringRes int actionResId, View.OnClickListener listener) {
    Snackbar.make(coordinator, getString(testResId), Snackbar.LENGTH_INDEFINITE)
        .setAction(getString(actionResId), listener)
        .show();
  }

  private void startPermissionRequest() {
    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_CODE_PERMISSION);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == REQUEST_CODE_PERMISSION) {
      if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        getLastLocation();
      } else {
        showSnackbar(R.string.location_permission_rationale, R.string.label_settings, view -> {
          Intent intent = new Intent();
          intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
          intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
        });
      }
    }
  }

  @SuppressWarnings("MissingPermission")
  private void getLastLocation() {
    provider.getLastLocation()
        .addOnCompleteListener(this, task -> {
          if (task.isSuccessful() && task.getResult() != null) {
            fetchNearbyResults(check(task.getResult()));
          } else {
            showSnackbar(R.string.error_location_unavailable);
          }
        });
  }

  @Override
  public void onBackPressed() {
    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
      getSupportFragmentManager().popBackStack();
      return;
    }

    super.onBackPressed();
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

  private void fetchNearbyResults(Location location) {
    Disposable disposable = client.search(location, page++, CARD_PAGE_SIZE)
        .flatMapObservable(search -> Observable.fromIterable(search.getResults()))
        .subscribe(expose -> {
          FloatingCardView card = new FloatingCardView(HomeActivity.this);
          stack.add(card);

          card.bind(expose);
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
      case android.R.id.home:
        onBackPressed();
        return true;

      case R.id.action_preferences:
        reporting.reportPreferences();
        PreferenceActivity.start(this);
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void onTopCardClicked(Expose expose) {
    GalleryFragment fragment = new GalleryFragment();
    fragment.bind(expose.getPictures());
    fragment.setRetainInstance(true);
    reporting.reportGallery(expose);
    addFragment(fragment);
  }

  private void addFragment(Fragment fragment) {
    getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom)
        .add(R.id.coordinator, fragment)
        .addToBackStack(null)
        .commit();
  }

  @OnClick(R.id.action_like)
  void onLikeClick() {
    if (!stack.hasChildren()) {
      showSnackbar(R.string.error_listings_unavailable);
      return;
    }

    FloatingCardView view = stack.getTopChild();
    reporting.reportLike(view.getExpose(), false);
    view.approve();
  }

  @OnClick(R.id.action_pass)
  void onPassClick() {
    if (!stack.hasChildren()) {
      showSnackbar(R.string.error_listings_unavailable);
      return;
    }

    FloatingCardView view = stack.getTopChild();
    reporting.reportLike(view.getExpose(), false);
    view.dismiss();
  }

  private void onMatch(Expose expose) {
    MatchFragment fragment = new MatchFragment();
    reporting.reportMatch(expose);
    fragment.setExpose(expose);
    addFragment(fragment);
  }
}
