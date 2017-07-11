package de.scout.fireplace.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import de.scout.fireplace.R;
import de.scout.fireplace.activity.AbstractFragment;
import de.scout.fireplace.models.Expose;
import de.scout.fireplace.ui.CircleTransform;

public class MatchFragment extends AbstractFragment {

  private static final String EXPOSE_URI = "https://www.immobilienscout24.de/expose/%s";

  @Nullable
  private Expose.Summary summary;

  @BindView(R.id.avatar) ImageView avatar;

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_match;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (summary != null) {
      Picasso.with(getContext())
          .load(summary.getImage())
          .transform(new CircleTransform())
          .into(avatar, new Callback() {
            @Override
            public void onSuccess() {
              ViewCompat.setElevation(avatar, R.dimen.action_elevation);
            }

            @Override
            public void onError() {
              avatar.setVisibility(View.GONE);
            }
          });
    }
  }

  void bind(Expose.Summary summary) {
    this.summary = summary;
  }

  @OnClick(R.id.action_view)
  void onViewClick() {
    if (summary == null) {
      return;
    }

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(String.format(EXPOSE_URI, summary.getId())));
    getActivity().startActivity(intent);
  }

  @OnClick(R.id.action_continue)
  void onContinueClick() {
    getActivity()
        .getSupportFragmentManager()
        .beginTransaction()
        .remove(this)
        .commit();
  }
}
