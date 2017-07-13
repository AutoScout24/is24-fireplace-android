package de.scout.fireplace.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnTextChanged;
import de.scout.fireplace.R;
import de.scout.fireplace.activity.AbstractActivity;
import javax.inject.Inject;

public class SettingsActivity extends AbstractActivity {

  @BindView(R.id.toolbar) Toolbar toolbar;

  @BindView(R.id.min_price) EditText minPrice;
  @BindView(R.id.max_price) EditText maxPrice;
  @BindView(R.id.number_rooms) EditText rooms;

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
    setUpNumberRoomsInput();
    setUpFurtherCriteria();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_preference;
  }

  private void setUpSupportActionBar(ActionBar actionBar) {
    actionBar.setDisplayHomeAsUpEnabled(true);
  }

  @OnTextChanged(R.id.min_price)
  void onMinPriceChanged(Editable editable) {
    repository.setMinPrice(parseInt(editable.toString()));
  }

  @OnTextChanged(R.id.max_price)
  void onMaxPriceChanged(Editable editable) {
    repository.setMaxPrice(parseInt(editable.toString()));
  }

  @OnTextChanged(R.id.number_rooms)
  void onNumberRoomsChanged(Editable editable) {
    repository.setNumberRooms(parseInt(editable.toString()));
  }

  private int parseInt(String string) {
    return Integer.parseInt(string.replaceAll("[^\\d]", ""));
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
    minPrice.setText(String.valueOf(repository.getMinPrice()));
    minPrice.setFilters(new InputFilter[] { new CurrencyFormatInputFilter() });

    maxPrice.setText(String.valueOf(repository.getMaxPrice()));
    maxPrice.setFilters(new InputFilter[] { new CurrencyFormatInputFilter() });
  }

  private void setUpNumberRoomsInput() {
    rooms.setText(String.valueOf(repository.getNumberRooms()));
    rooms.setFilters(new InputFilter[] { new NumberRangeInputFilter(1, 25) });
  }

  private void setUpFurtherCriteria() {
    balcony.setChecked(repository.hasBalcony());
    kitchen.setChecked(repository.hasKitchen());
  }
}
