package com.leicui.leicui.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leicui.leicui.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TitleLayout extends LinearLayout {

  @ColorInt
  private int mTextColor;

  @BindView(R.id.title_text)
  TextView mTitleTextView;

  public TitleLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater.from(context).inflate(R.layout.title, this);

    ButterKnife.bind(this);

    init(attrs);
  }

  @OnClick(R.id.title_back)
  public void clickBack() {
    ((Activity) getContext()).finish();
  }

  @OnClick(R.id.title_edit)
  public void clickEdit(View view) {
    Toast.makeText(getContext(), "Edit clicked", Toast.LENGTH_SHORT).show();
  }

  @OnClick(R.id.title_text)
  public void clickText(View view) {
    Toast.makeText(getContext(), "Text clicked", Toast.LENGTH_SHORT).show();
  }

  private void init(AttributeSet attrs) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleLayout);
    mTextColor = typedArray.getColor(R.styleable.TitleLayout_title_text_color, Color.WHITE);
    mTitleTextView.setTextColor(mTextColor);
    typedArray.recycle();
  }

}
