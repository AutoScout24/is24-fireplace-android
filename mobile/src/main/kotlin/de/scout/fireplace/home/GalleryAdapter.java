package de.scout.fireplace.home;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import de.scout.fireplace.R;
import de.scout.fireplace.models.Expose;
import java.util.List;

public class GalleryAdapter extends PagerAdapter {

  private final List<Expose.Picture> pictures;

  GalleryAdapter(List<Expose.Picture> pictures) {
    this.pictures = pictures;
  }

  @Override
  public int getCount() {
    return pictures.size();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_gallery, container, false);
    View parent = (View) container.getParent();

    Picasso.with(view.getContext())
        .load(pictures.get(position).getUrl(parent.getWidth(), parent.getHeight()))
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
