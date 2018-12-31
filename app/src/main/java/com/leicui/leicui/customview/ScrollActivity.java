package com.leicui.leicui.customview;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.leicui.leicui.R;

import java.util.ArrayList;
import java.util.List;

public class ScrollActivity extends AppCompatActivity {

  private List<String> mList;
  private LinearLayoutManager layoutManager;
  private MyAdapter mMyAdapter;
  private RecyclerView recyclerView;
  private Handler mMainHandler;
  private boolean mIsLoading = false;
  private int mGroupNumber = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_flood_fill);
    mMainHandler = new Handler(getMainLooper());

    mList = new ArrayList<>();
    addItems();

    recyclerView = findViewById(R.id.recycler_view);
    layoutManager = new LinearLayoutManager(this);
    mMyAdapter = new MyAdapter();
    recyclerView.setAdapter(mMyAdapter);
    recyclerView.setLayoutManager(layoutManager);

    recyclerView.addOnScrollListener(
        new RecyclerView.OnScrollListener() {
          @Override
          public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
          }

          @Override
          public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount(); // == mMyAdapter.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 2
                && !mIsLoading) {
              mIsLoading = true;
              mList.add("");                                  // Fake data for footer progress spinner
              mMyAdapter.notifyDataSetChanged();
              mMainHandler.postDelayed(
                  new Runnable() {
                    @Override
                    public void run() {
                      mList.remove(mList.size() - 1);
                      addItems();
                      mIsLoading = false;
                      mMyAdapter.notifyDataSetChanged();
                    }
                  },
                  3000);
            }
          }
        });
  }

  private void addItems() {
    for (int i = 0; i < 20; i++) {
      mList.add("Hello Lei - " + mGroupNumber + " - " + i);
    }
    mGroupNumber++;
  }

  class MyAdapter extends RecyclerView.Adapter<BaseHolder> {

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
      if (viewType == 0) {
        View view = getLayoutInflater().inflate(R.layout.item_scroll_text, viewGroup, false);
        return new ItemHolder(view);
      } else if (viewType == 1) {
        View view = getLayoutInflater().inflate(R.layout.item_scroll_header, viewGroup, false);
        return new HeaderHolder(view);
      } else {
        View view =
            getLayoutInflater().inflate(R.layout.item_scroll_progress_bar, viewGroup, false);
        return new FooterHolder(view);
      }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int i) {
      holder.bind(mList.get(i));
    }

    @Override
    public int getItemCount() {
      return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
      if (position == mList.size() - 1) {
        return 2;
      }
      if (position % 20 == 0) {
        return 1;
      }
      return 0;
    }
  }

  abstract class BaseHolder extends RecyclerView.ViewHolder {

    BaseHolder(@NonNull View itemView) {
      super(itemView);
    }

    public abstract void bind(String str);
  }

  class ItemHolder extends BaseHolder {

    private TextView mTextView;

    ItemHolder(@NonNull View itemView) {
      super(itemView);
      mTextView = itemView.findViewById(R.id.item_text);
    }

    @Override
    public void bind(String str) {
      mTextView.setText(str);
    }
  }

  class HeaderHolder extends BaseHolder {

    private Button mButton;

    HeaderHolder(@NonNull View itemView) {
      super(itemView);
      mButton = itemView.findViewById(R.id.button);
      mButton.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              mList.remove(1);
              mMyAdapter.notifyItemRemoved(1);
              mMyAdapter.notifyItemMoved(1, 3);
            }
          });
    }

    @Override
    public void bind(String str) {
      mButton.setText(str);
    }
  }

  class FooterHolder extends BaseHolder {

    FooterHolder(@NonNull View itemView) {
      super(itemView);
    }

    @Override
    public void bind(String str) {}
  }
}
