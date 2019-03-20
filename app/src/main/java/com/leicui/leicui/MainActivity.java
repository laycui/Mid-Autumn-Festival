package com.leicui.leicui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leicui.leicui.customview.CustomViewActivity;
import com.leicui.leicui.customview.FloodFillActivity;
import com.leicui.leicui.customview.NestedActivity;
import com.leicui.leicui.customview.PagerActivity;
import com.leicui.leicui.customview.ScrollActivity;
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

  @BindView(R.id.container)
  ConstraintLayout mContainer;

  @BindView(R.id.to_custom_view)
  Button mButtonCustomView;

  private BottomSheetBehavior mBottomSheetBehavior;

  @Inject MyExample mMyExample;

  @BindView(R.id.navigation)
  BottomNavigationView mNavigationView;

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
    mNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    // Dagger:
    ((MyApplication) getApplication()).getMyComponent().inject(this);
    TextView textView = findViewById(R.id.date_text);
    textView.setText(String.valueOf(mMyExample.getDate()));

    initBottomSheetBehavior();

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);

    Button mFloodButton = findViewById(R.id.flood_btn);
    mFloodButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, FloodFillActivity.class);
            startActivity(intent);
          }
        });

    Button scrollButton = findViewById(R.id.scroll_btn);
    scrollButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, ScrollActivity.class);
            startActivity(intent);
          }
        });

    Button nestedButton = findViewById(R.id.nest_recycler_btn);
    nestedButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, NestedActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      Slide slide = new Slide();
      slide.setSlideEdge(Gravity.START);
      TransitionManager.beginDelayedTransition(mContainer, slide);
      if (mNavigationView.getVisibility() == View.VISIBLE) {
        mNavigationView.setVisibility(View.INVISIBLE);
      } else {
        mNavigationView.setVisibility(View.VISIBLE);
      }
    }
    return super.onOptionsItemSelected(item);
  }

  @OnClick(R.id.to_recycler_view)
  public void clickRecyclerViewButton(View view) {
    Intent intent = new Intent(this, RecyclerViewActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.to_custom_view)
  public void clickCustomView(View view) {
    Bundle bundle =
        ActivityOptions.makeSceneTransitionAnimation(
                this, mButtonCustomView, mButtonCustomView.getTransitionName())
            .toBundle();

    Intent intent = new Intent(this, CustomViewActivity.class);
    startActivity(intent, bundle);
  }

  @OnClick(R.id.tag)
  public void clickTag(View view) {
    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();

    Intent intent = new Intent(this, TagViewActivity.class);
    startActivity(intent, bundle);
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
                Snackbar.make(mContainer, "msg: " + msg, Snackbar.LENGTH_LONG).show();
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
