package com.leicui.leicui.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leicui.leicui.MyApplication;
import com.leicui.leicui.R;
import com.leicui.leicui.customview.CustomViewActivity;
import com.leicui.leicui.customview.NestedActivity;
import com.leicui.leicui.customview.PagerActivity;
import com.leicui.leicui.customview.TagViewActivity;
import com.leicui.leicui.dagger.MyExample;
import com.leicui.leicui.databindingrecyclerview.RecyclerViewActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainFragment extends Fragment {

  @BindView(R.id.bottom_sheet)
  LinearLayout mBottomSheet;

  @BindView(R.id.touch_outside)
  View mTouchOutside;

  @BindView(R.id.container)
  ConstraintLayout mContainer;

  @BindView(R.id.to_custom_view)
  Button mButtonCustomView;

  @Inject MyExample mMyExample;

  private BottomSheetBehavior mBottomSheetBehavior;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_main, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    // ButterKnife
    ButterKnife.bind(this, getView());

    initBottomSheetBehavior();

    Button nestedButton = getView().findViewById(R.id.nest_recycler_btn);
    nestedButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(getContext(), NestedActivity.class);
            startActivity(intent);
          }
        });

    // Dagger:
    ((MyApplication) getActivity().getApplication()).getMyComponent().inject(this);
    TextView textView = getView().findViewById(R.id.date_text);
    textView.setText(String.valueOf(mMyExample.getDate()));
  }

  @OnClick(R.id.to_recycler_view)
  public void clickRecyclerViewButton(View view) {
    Intent intent = new Intent(getContext(), RecyclerViewActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.to_custom_view)
  public void clickCustomView(View view) {
    Bundle bundle =
        ActivityOptions.makeSceneTransitionAnimation(
                getActivity(), mButtonCustomView, mButtonCustomView.getTransitionName())
            .toBundle();

    Intent intent = new Intent(getActivity(), CustomViewActivity.class);
    startActivity(intent, bundle);
  }

  @OnClick(R.id.tag)
  public void clickTag(View view) {
    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();

    Intent intent = new Intent(getContext(), TagViewActivity.class);
    startActivity(intent, bundle);
  }

  @OnClick(R.id.handler)
  public void clickHandler(View view) {
    startActivity(new Intent(getContext(), PagerActivity.class));

    HandlerThread longPollThread = new HandlerThread("LongPollThread");
    longPollThread.start();
    Handler longPollHandler =
        new Handler(
            longPollThread.getLooper(),
            new Handler.Callback() {
              @Override
              public boolean handleMessage(Message msg) {
                Snackbar.make(mContainer, "msg: " + msg, Snackbar.LENGTH_LONG).show();
                return false;
              }
            });
    longPollHandler.sendEmptyMessage(101);
  }

  public boolean dispatchTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
        Rect outRect = new Rect();
        mBottomSheet.getGlobalVisibleRect(outRect);

        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
          mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
          return true;
        }
      }
    }

    return false;
  }

  private void initBottomSheetBehavior() {
    mBottomSheet = getView().findViewById(R.id.bottom_sheet);
    mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
    assert (mBottomSheetBehavior != null);
    mTouchOutside.setAlpha(0f);
    mBottomSheetBehavior.setBottomSheetCallback(
        new BottomSheetBehavior.BottomSheetCallback() {
          @Override
          public void onStateChanged(@NonNull View bottomSheet, int newState) {}

          @Override
          public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            mTouchOutside.setAlpha(slideOffset);
          }
        });
  }

  public boolean onBackPressed() {
    if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
      mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
      return true;
    }
    return false;
  }
}
