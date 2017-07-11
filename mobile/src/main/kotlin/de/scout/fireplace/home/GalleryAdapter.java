package de.scout.fireplace.home;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import de.scout.fireplace.R;
import java.util.List;

public class GalleryAdapter extends PagerAdapter {

  private final List<String> images;

  GalleryAdapter(List<String> images) {
    this.images = images;
  }

  @Override
  public int getCount() {
    return images.size();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_gallery, container, false);

    Picasso.with(view.getContext())
        .load(images.get(position))
        .fit()
        .centerCrop()
        .into(ButterKnife.<ImageView>findById(view, R.id.image));

    container.addView(view);
    return view;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }
}
