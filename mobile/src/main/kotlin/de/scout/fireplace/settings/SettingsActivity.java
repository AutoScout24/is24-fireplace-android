package de.scout.fireplace.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import de.scout.fireplace.R;
import de.scout.fireplace.activity.AbstractActivity;
import javax.inject.Inject;

public class SettingsActivity extends AbstractActivity {

  @BindView(R.id.toolbar) Toolbar toolbar;

  @BindView(R.id.price) NumericRangeView price;
  @BindView(R.id.space) NumericRangeView space;
  @BindView(R.id.rooms) NumericRangeView rooms;

  @BindView(R.id.criteria_kitchen) Switch kitchen;
  @BindView(R.id.criteria_garage) Switch garage;
  @BindView(R.id.criteria_cellar) Switch cellar;
  @BindView(R.id.criteria_lift) Switch lift;
  @BindView(R.id.criteria_balcony) Switch balcony;
  @BindView(R.id.criteria_garden) Switch garden;
  @BindView(R.id.criteria_new_build) Switch newBuild;

  @Inject SettingsRepository repository;

  public static void start(Context context) {
    context.startActivity(new Intent(context, SettingsActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(toolbar);
    setUpSupportActionBar(getSupportActionBar());

    setUpPriceRangeInput();
    setUpSpaceRangeInput();
    setUpRoomsRangeInput();
    setUpFurtherCriteria();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_settings;
  }

  private void setUpSupportActionBar(ActionBar actionBar) {
    actionBar.setDisplayHomeAsUpEnabled(true);
  }

  @OnCheckedChanged(R.id.criteria_kitchen)
  void onKitchenCheckedChanged(CompoundButton button, boolean checked) {
    repository.hasKitchen(checked);
  }

  @OnCheckedChanged(R.id.criteria_garage)
  void onGarageCheckedChanged(CompoundButton button, boolean checked) {
    repository.hasGarage(checked);
  }

  @OnCheckedChanged(R.id.criteria_cellar)
  void onCellarCheckedChange(CompoundButton button, boolean checked) {
    repository.hasCellar(checked);
  }

  @OnCheckedChanged(R.id.criteria_lift)
  void onLiftCheckedChange(CompoundButton button, boolean checked) {
    repository.hasLift(checked);
  }

  @OnCheckedChanged(R.id.criteria_balcony)
  void onBalconyCheckedChange(CompoundButton button, boolean checked) {
    repository.hasBalcony(checked);
  }

  @OnCheckedChanged(R.id.criteria_garden)
  void onGardenCheckedChange(CompoundButton button, boolean checked) {
    repository.hasGarden(checked);
  }

  @OnCheckedChanged(R.id.criteria_new_build)
  void onNewBuildCheckedChange(CompoundButton button, boolean checked) {
    repository.isNewBuild(checked);
  }

  private void setUpPriceRangeInput() {
    price.setMinValue(repository.getMinPrice());
    price.setMaxValue(repository.getMaxPrice());

    price.setFilter(new CurrencyFormatInputFilter());

    price.addOnMinChangeListener(value -> repository.setMinPrice((int) value));
    price.addOnMaxChangeListener(value -> repository.setMaxPrice((int) value));
  }

  private void setUpSpaceRangeInput() {
    space.setMinValue(repository.getMinSpace());
    space.setMaxValue(repository.getMaxSpace());

    space.setFilter(new NumberRangeInputFilter(10, 500));

    space.addOnMinChangeListener(value -> repository.setMinSpace((int) value));
    space.addOnMaxChangeListener(value -> repository.setMaxSpace((int) value));
  }

  private void setUpRoomsRangeInput() {
    rooms.setMinValue(repository.getMinRooms());
    rooms.setMaxValue(repository.getMaxRooms());

    rooms.setFilter(new NumberRangeInputFilter(1, 25));

    rooms.addOnMinChangeListener(value -> repository.setMinRooms((int) value));
    rooms.addOnMaxChangeListener(value -> repository.setMaxRooms((int) value));
  }

  private void setUpFurtherCriteria() {
    balcony.setChecked(repository.hasBalcony());
    kitchen.setChecked(repository.hasKitchen());
  }
}
