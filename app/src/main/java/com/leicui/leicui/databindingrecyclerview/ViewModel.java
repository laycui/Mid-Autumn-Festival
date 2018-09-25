package com.leicui.leicui.databindingrecyclerview;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.leicui.leicui.BR;
import com.leicui.leicui.R;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class ViewModel {
  public final ObservableList<String> items = new ObservableArrayList<>();
  public final ItemBinding<String> itemBinding = ItemBinding.of(BR.item, R.layout.item);
}
