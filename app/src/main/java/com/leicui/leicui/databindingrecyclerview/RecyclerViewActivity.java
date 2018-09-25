package com.leicui.leicui.databindingrecyclerview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.leicui.leicui.R;
import com.leicui.leicui.databinding.ActivityRecyclerViewBinding;

public class RecyclerViewActivity extends AppCompatActivity {

  ViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityRecyclerViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view);

    viewModel = new ViewModel();

    binding.setViewModel(viewModel);

    for (int i = 0; i < 100; i++) {
      viewModel.items.add("Item " + i);
    }
  }
}
