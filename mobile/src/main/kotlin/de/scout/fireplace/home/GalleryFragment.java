package de.scout.fireplace.home;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.BindView;
import de.scout.fireplace.R;
import de.scout.fireplace.activity.AbstractFragment;
import de.scout.fireplace.models.Expose;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class GalleryFragment extends AbstractFragment {

  private PagerAdapter adapter;

  @BindView(R.id.pager) ViewPager pager;

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_gallery;
  }

  public void bind(List<Expose.Picture> pictures) {
    this.adapter = new GalleryAdapter(pictures);
  }

  @Override
  public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    pager.setAdapter(adapter);
  }
}
