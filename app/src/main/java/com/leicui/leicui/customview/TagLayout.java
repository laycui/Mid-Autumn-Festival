package com.leicui.leicui.customview;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class TagLayout extends ViewGroup {
  int deviceWidth;

  public TagLayout(Context context) {
    this(context, null, 0);
  }

  public TagLayout(Context context, AttributeSet attributeSet) {
    this(context, attributeSet, 0);
  }

  public TagLayout(Context context, AttributeSet attributeSet, int defStyleAttr) {
    super(context, attributeSet, defStyleAttr);
    init(context);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    final int count = getChildCount();
    int curWidth;
    int curHeight;
    int curLeft;
    int curTop;
    int maxHeight;

    final int childLeft = getPaddingLeft();
    final int childTop = getPaddingTop();
    final int childRight = getMeasuredWidth() - getPaddingRight();
    final int childBottom = getMeasuredHeight() - getPaddingBottom();
    final int childWidth = childRight - childLeft;
    final int childHeight = childBottom - childTop;

    maxHeight = 0;
    curLeft = childLeft;
    curTop = childTop;

    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);
      if (child.getVisibility() == GONE) {
        return;
      }

      child.measure(
          MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
          MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));

      curWidth = child.getMeasuredWidth();
      curHeight = child.getMeasuredHeight();

      if (curLeft + curWidth >= childRight) {
        curLeft = childLeft;
        curTop += maxHeight;
        maxHeight = 0;
      }
      child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
      if (maxHeight < curHeight) {
        maxHeight = curHeight;
      }
      curLeft += curWidth;
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int count = getChildCount();
    // Measurement will ultimately be computing these values.
    int maxHeight = 0;
    int maxWidth = 0;
    int childState = 0;
    int mLeftWidth = 0;
    int rowCount = 0;
    // Iterate through all children, measuring them and computing our dimensions
    // from their size.
    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);
      if (child.getVisibility() == GONE)
        continue;
      // Measure the child.
      measureChild(child, widthMeasureSpec, heightMeasureSpec);
      maxWidth += Math.max(maxWidth, child.getMeasuredWidth());
      mLeftWidth += child.getMeasuredWidth();
      if ((mLeftWidth / deviceWidth) > rowCount) {
        maxHeight += child.getMeasuredHeight();
        rowCount++;
      } else {
        maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
      }
      childState = combineMeasuredStates(childState, child.getMeasuredState());
    }
    // Check against our minimum height and width
    maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
    maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
    // Report our final dimensions.
    setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
            resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
  }

  private void init(Context context) {
    final Display display =
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    Point deviceDisplay = new Point();
    display.getSize(deviceDisplay);
    deviceWidth = deviceDisplay.x;
  }
}
