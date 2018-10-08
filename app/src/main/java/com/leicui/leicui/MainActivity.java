package com.leicui.leicui;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leicui.leicui.customview.CustomViewActivity;
import com.leicui.leicui.customview.PagerActivity;
import com.leicui.leicui.customview.TagViewActivity;
import com.leicui.leicui.dagger.MyExample;
import com.leicui.leicui.databindingrecyclerview.RecyclerViewActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.message)
  TextView mTextMessage;

  @BindView(R.id.bottom_sheet)
  LinearLayout mBottomSheet;

  @BindView(R.id.touch_outside)
  View mTouchOutside;

  private BottomSheetBehavior mBottomSheetBehavior;

  @Inject MyExample mMyExample;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
      (item) -> {
        switch (item.getItemId()) {
          case R.id.navigation_home:
            mTextMessage.setText(R.string.title_home);
            return true;
          case R.id.navigation_dashboard:
            mTextMessage.setText(R.string.title_dashboard);
            return true;
          case R.id.navigation_notifications:
            mTextMessage.setText(R.string.title_notifications);
            return true;
        }
        return false;
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // ButterKnife
    ButterKnife.bind(this);

    mTextMessage = findViewById(R.id.message);
    BottomNavigationView navigation = findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    // Dagger:
    ((MyApplication) getApplication()).getMyComponent().inject(this);
    TextView textView = findViewById(R.id.date_text);
    textView.setText(String.valueOf(mMyExample.getDate()));

    initBottomSheetBehavior();
  }

  @OnClick(R.id.to_recycler_view)
  public void clickRecyclerViewButton(View view) {
    Intent intent = new Intent(this, RecyclerViewActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.to_custom_view)
  public void clickCustomView(View view) {
    Intent intent = new Intent(this, CustomViewActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.tag)
  public void clickTag(View view) {
    Intent intent = new Intent(this, TagViewActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.handler)
  public void clickHandler(View view) {
    startActivity(new Intent(this, PagerActivity.class));

    HandlerThread longPollThread = new HandlerThread("LongPollThread");
    longPollThread.start();
    Handler longPollHandler =
        new Handler(
            longPollThread.getLooper(),
            new Handler.Callback() {
              @Override
              public boolean handleMessage(Message msg) {
                Snackbar.make(mBottomSheet, "msg: " + msg, Snackbar.LENGTH_LONG).show();
                return false;
              }
            });
    longPollHandler.sendEmptyMessage(101);
  }

  @Override
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

    return super.dispatchTouchEvent(event);
  }

  private void initBottomSheetBehavior() {
    mBottomSheet = findViewById(R.id.bottom_sheet);
    mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
    assert (mBottomSheetBehavior != null);
    mTouchOutside.setAlpha(0f);
    mBottomSheetBehavior.setBottomSheetCallback(
        new BottomSheetBehavior.BottomSheetCallback() {
          @Override
          public void onStateChanged(@NonNull View bottomSheet, int newState) {
          }

          @Override
          public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            mTouchOutside.setAlpha(slideOffset);
          }
        });
  }

  @Override
  public void onBackPressed() {
    if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
      mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    } else {
      super.onBackPressed();
    }
  }
}
