package de.scout.fireplace.preference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import de.scout.fireplace.R;
import de.scout.fireplace.activity.AbstractActivity;
import javax.inject.Inject;
import org.florescu.android.rangeseekbar.RangeSeekBar;

public class PreferenceActivity extends AbstractActivity implements RangeSeekBar.OnRangeSeekBarChangeListener, NumberPicker.OnValueChangeListener {

  @BindView(R.id.toolbar) Toolbar toolbar;

  @BindView(R.id.number_rooms) NumberPicker rooms;
  @BindView(R.id.switch_balcony) Switch balcony;
  @BindView(R.id.switch_kitchen) Switch kitchen;
  @BindView(R.id.price_range) TextView prices;

  @Inject PreferenceRepository repository;

  public static void start(Context context) {
    context.startActivity(new Intent(context, PreferenceActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(toolbar);
    setUpSupportActionBar(getSupportActionBar());

    initPriceRangeSeekBar();
    initAddtionalSwitches();
    initNumberOfRoomsPicker();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_preference;
  }

  private void setUpSupportActionBar(ActionBar actionBar) {
    actionBar.setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public void onRangeSeekBarValuesChanged(RangeSeekBar rangeSeekBar, Object o, Object t1) {
    repository.setMinPrice(rangeSeekBar.getSelectedMinValue().intValue());
    repository.setMaxPrice(rangeSeekBar.getSelectedMaxValue().intValue());
    updateRanges(rangeSeekBar);
  }

  private void updateRanges(RangeSeekBar<Integer> seek) {
    prices.setText(String.format("%s - %s €", seek.getSelectedMinValue(), seek.getSelectedMaxValue()));
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

  private void initNumberOfRoomsPicker() {
    rooms.setMinValue(1);
    rooms.setMaxValue(10);
    rooms.setValue(repository.getMinRooms());
    rooms.setOnValueChangedListener(this);
  }

  private void initAddtionalSwitches() {
    balcony.setChecked(repository.hasBalcony());
    kitchen.setChecked(repository.hasKitchen());
  }

  private void initPriceRangeSeekBar() {
    RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<>(this);
    // Set the range
    rangeSeekBar.setRangeValues(100, 3000);
    rangeSeekBar.setSelectedMinValue(repository.getMinPrice());
    rangeSeekBar.setSelectedMaxValue(repository.getMaxPrice());

    rangeSeekBar.setOnRangeSeekBarChangeListener(this);
    // Add to layout
    FrameLayout layout = (FrameLayout) findViewById(R.id.seekbar_placeholder);
    layout.addView(rangeSeekBar);
    updateRanges(rangeSeekBar);
  }

}
