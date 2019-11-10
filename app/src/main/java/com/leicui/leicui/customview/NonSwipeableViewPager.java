package com.leicui.leicui.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSwipeableViewPager extends ViewPager {
  private boolean mSwipingEnabled = true;

  public NonSwipeableViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setSwipingEnabled(boolean pagingEnabled) {
    mSwipingEnabled = pagingEnabled;
  }

  public boolean isSwipingEnabled() {
    return mSwipingEnabled;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    if (mSwipingEnabled) {
      return super.onInterceptTouchEvent(event);
    }

    // Never allow swiping to switch between pages
    return false;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (mSwipingEnabled) {
      return super.onTouchEvent(event);
    }

    // Never allow swiping to switch between pages
    return false;
  }
}
