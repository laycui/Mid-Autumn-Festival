package com.leicui.leicui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leicui.leicui.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPagerFragment extends Fragment {

  private static final String ORDER = "order";

  @BindView(R.id.order)
  TextView mOrderText;

  public static ViewPagerFragment newInstance(int order) {
    Bundle args = new Bundle();
    args.putInt(ORDER, order);
    ViewPagerFragment fragment = new ViewPagerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  private int mOrder;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mOrder = getArguments().getInt(ORDER);
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_pager, container, false);
    ButterKnife.bind(this, root);

    mOrderText.setText(String.valueOf(mOrder));
    mOrderText.setBackgroundColor(getColor());

    return root;
  }

  public int getColor() {
    int res = mOrder % 3;
    switch (res) {
      case 0:
        return Color.RED;
      case 1:
        return Color.BLUE;
      case 2:
      default:
        return Color.GREEN;
    }
  }
}
