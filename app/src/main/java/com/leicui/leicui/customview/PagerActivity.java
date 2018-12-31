package com.leicui.leicui.customview;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.leicui.leicui.R;
import com.leicui.leicui.fragments.ViewPagerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PagerActivity extends AppCompatActivity {

  @BindView(R.id.view_pager)
  ViewPager mViewPager;

  @BindView(R.id.tabs)
  TabLayout mTabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pager);
    ButterKnife.bind(this);
    getSupportActionBar().setElevation(0f);

    mTabLayout.setupWithViewPager(mViewPager);
    mViewPager.setAdapter(
        new FragmentPagerAdapter(getSupportFragmentManager()) {
          @Override
          public Fragment getItem(int position) {
            return ViewPagerFragment.newInstance(position);
          }

          @Override
          public int getCount() {
            return 4;
          }

          @Override
          public CharSequence getPageTitle(int position) {
            return String.valueOf(position);
          }
        });
    mViewPager.setOffscreenPageLimit(3);
    mViewPager.setCurrentItem(2);
  }
}
