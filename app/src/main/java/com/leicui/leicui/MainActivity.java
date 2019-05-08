package com.leicui.leicui;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.leicui.leicui.fragments.FloodFillFragment;
import com.leicui.leicui.fragments.MainFragment;
import com.leicui.leicui.fragments.ScrollFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.container)
  ConstraintLayout mContainer;

  @BindView(R.id.view_pager)
  ViewPager mViewPager;

  private MainFragment mMainFragment;

  @BindView(R.id.navigation)
  BottomNavigationView mNavigationView;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
      (item) -> {
        switch (item.getItemId()) {
          case R.id.navigation_home:
            mViewPager.setCurrentItem(0, true);
            return true;
          case R.id.navigation_dashboard:
            mViewPager.setCurrentItem(1, true);
            return true;
          case R.id.navigation_notifications:
            mViewPager.setCurrentItem(2, true);
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

    mNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);

    FragmentPagerAdapter pagerAdapter =
        new FragmentPagerAdapter(getSupportFragmentManager()) {
          @Override
          public Fragment getItem(int position) {
            switch (position) {
              case 0:
                mMainFragment = new MainFragment();
                return mMainFragment;
              case 1:
                return new ScrollFragment();
              case 2:
              default:
                return new FloodFillFragment();
            }
          }

          @Override
          public int getCount() {
            return 3;
          }
        };

    mViewPager.setAdapter(pagerAdapter);
    mViewPager.addOnPageChangeListener(
        new ViewPager.OnPageChangeListener() {
          @Override
          public void onPageScrolled(
              int position, float positionOffset, int positionOffsetPixels) {}

          @Override
          public void onPageSelected(int position) {
            mNavigationView.setSelectedItemId(positionToNavigationMenuId(position));
          }

          @Override
          public void onPageScrollStateChanged(int state) {}
        });
    mViewPager.setOffscreenPageLimit(4);
  }

  private int positionToNavigationMenuId(int position) {
    switch (position) {
      case 0:
        return R.id.navigation_home;
      case 1:
        return R.id.navigation_dashboard;
      case 2:
      default:
        return R.id.navigation_notifications;
    }
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

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    if (mMainFragment != null && mMainFragment.dispatchTouchEvent(event)) {
      return true;
    }

    return super.dispatchTouchEvent(event);
  }

  @Override
  public void onBackPressed() {
    if (mMainFragment != null && mMainFragment.onBackPressed()) {
      return;
    }
    super.onBackPressed();
  }
}
