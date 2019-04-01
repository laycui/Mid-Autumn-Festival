package com.leicui.leicui.customview;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.leicui.leicui.R;

import java.util.ArrayList;
import java.util.List;

public class ScrollActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_flood_fill);

    RecyclerView recyclerView = findViewById(R.id.recycler_view);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    MyAdapter mMyAdapter = new MyAdapter();
    recyclerView.setAdapter(mMyAdapter);
    recyclerView.setLayoutManager(layoutManager);

    mMyAdapter.queueTask();
    recyclerView.addOnScrollListener(
        new RecyclerView.OnScrollListener() {
          @Override
          public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount(); // == mMyAdapter.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            if (firstVisibleItemPosition + visibleItemCount > totalItemCount - 2) {
              mMyAdapter.queueTask();
            }
          }
        });
  }

  static class MyAdapter extends RecyclerView.Adapter<BaseHolder> {

    private Handler mMainHandler;
    private List<Item> mList;
    private boolean mIsLoading;
    private int mGroupNumber = 0;

    MyAdapter() {
      mList = new ArrayList<>();
      mMainHandler = new Handler();
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view;
      LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
      switch (viewType) {
        case ViewType.HEADER:
          view = layoutInflater.inflate(R.layout.item_scroll_header, parent, false);
          return new HeaderHolder(view);
        case ViewType.ITEM:
          view = layoutInflater.inflate(R.layout.item_scroll_text, parent, false);
          return new ItemHolder(view);
        case ViewType.LOADING:
        default:
          view = layoutInflater.inflate(R.layout.item_scroll_progress_bar, parent, false);
          return new LoadingHolder(view);
      }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
      holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
      return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
      return mList.get(position).mType;
    }

    void queueTask() {
      if (mIsLoading) {
        return;
      }
      mIsLoading = true;
      if (!mList.isEmpty()) {
        mList.add(new Item(null, ViewType.LOADING));
      }
      notifyDataSetChanged();
      mMainHandler.postDelayed(
          () -> {
            List<Item> list = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
              int num = i + 1;
              list.add(new Item("Item " + num, ViewType.ITEM));
            }
            addItems(list);
          },
          1000);
    }

    void addItems(List<Item> items) {
      if (!mList.isEmpty()) {
        mList.remove(mList.size() - 1);
      }
      mIsLoading = false;
      mList.add(new Item("Header - " + mGroupNumber++, ViewType.HEADER));
      mList.addAll(items);
      notifyDataSetChanged();
    }
  }

  abstract static class BaseHolder extends RecyclerView.ViewHolder {
    BaseHolder(View itemView) {
      super(itemView);
    }

    abstract void bind(Item item);
  }

  static class HeaderHolder extends BaseHolder {
    Button mButton;

    HeaderHolder(View itemView) {
      super(itemView);
      mButton = itemView.findViewById(R.id.button);
    }

    @Override
    void bind(Item item) {
      mButton.setText(item.mName);
    }
  }

  static class ItemHolder extends BaseHolder {
    TextView mTextView;

    ItemHolder(View itemView) {
      super(itemView);
      mTextView = itemView.findViewById(R.id.item_text);
    }

    @Override
    void bind(Item item) {
      mTextView.setText(item.mName);
    }
  }

  static class LoadingHolder extends BaseHolder {

    LoadingHolder(View itemView) {
      super(itemView);
    }

    @Override
    void bind(Item item) {}
  }

  static class Item {
    private String mName;
    private @ViewType int mType;

    Item(String name, int type) {
      mName = name;
      mType = type;
    }
  }

  @IntDef(value = {ViewType.HEADER, ViewType.ITEM, ViewType.LOADING})
  @interface ViewType {
    int HEADER = 0;
    int ITEM = 1;
    int LOADING = 2;
  }
}
