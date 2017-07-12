package de.scout.fireplace.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import de.scout.fireplace.R;
import de.scout.fireplace.activity.AbstractActivity;
import javax.inject.Inject;

public class SettingsActivity extends AbstractActivity implements NumberPicker.OnValueChangeListener {

  @BindView(R.id.toolbar) Toolbar toolbar;

  @BindView(R.id.min_price) EditText minPrice;
  @BindView(R.id.max_price) EditText maxPrice;
  @BindView(R.id.number_rooms) EditText rooms;

  @BindView(R.id.switch_balcony) Switch balcony;
  @BindView(R.id.switch_kitchen) Switch kitchen;

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

  @OnCheckedChanged(R.id.switch_balcony)
  void onBalconyCheckedChanged(CompoundButton button, boolean checked) {
    repository.hasBalcony(checked);
  }

  @OnCheckedChanged(R.id.switch_kitchen)
  void onKitchenCheckedChanged(CompoundButton button, boolean checked) {
    repository.hasKitchen(checked);
  }

  @Override
  public void onValueChange(NumberPicker numberPicker, int i, int i1) {
    repository.setMinRooms(i1);
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
