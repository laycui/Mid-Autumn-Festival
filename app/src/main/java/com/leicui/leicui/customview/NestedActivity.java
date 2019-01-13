package com.leicui.leicui.customview;

import android.os.Bundle;
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

public class NestedActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_flood_fill);

    RecyclerView recyclerView = findViewById(R.id.recycler_view);
    recyclerView.setAdapter(new MyAdapter());
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  static class MyAdapter extends RecyclerView.Adapter<BaseHolder> {

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
      View root;
      if (viewType == 0) {
        root = layoutInflater.inflate(R.layout.item_scroll_text, parent, false);
        return new TextHolder(root);
      } else {
        root = layoutInflater.inflate(R.layout.activity_flood_fill, parent, false);
        return new ButtonsHolder(root);
      }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
      if (position % 2 == 0) {
        holder.bind("Group " + position / 2);
      } else if (position % 4 == 3) {
        holder.bind("1");
      } else {
        holder.bind("0");
      }
    }

    @Override
    public int getItemCount() {
      return 40;
    }

    @Override
    public int getItemViewType(int position) {
      return position % 2;
    }
  }

  abstract static class BaseHolder extends RecyclerView.ViewHolder {

    BaseHolder(View itemView) {
      super(itemView);
    }

    abstract void bind(String text);
  }

  static class TextHolder extends BaseHolder {

    private TextView mTextView;

    TextHolder(View itemView) {
      super(itemView);
      mTextView = itemView.findViewById(R.id.item_text);
    }

    @Override
    void bind(String text) {
      mTextView.setText(text);
    }
  }

  static class ButtonsHolder extends BaseHolder {

    RecyclerView mSubRecyclerView;

    ButtonsHolder(View itemView) {
      super(itemView);
      mSubRecyclerView = itemView.findViewById(R.id.recycler_view);
      mSubRecyclerView.setAdapter(new SubAdapter());
      mSubRecyclerView.setLayoutManager(
          new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false));
      mSubRecyclerView.setLayoutParams(
          new ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    void bind(String text) {
      if ("1".equals(text)) {
        mSubRecyclerView.scrollToPosition(mSubRecyclerView.getAdapter().getItemCount() - 1);
      } else {
        mSubRecyclerView.scrollToPosition(0);
      }
    }
  }

  static class SubAdapter extends RecyclerView.Adapter<SubHolder> {

    @NonNull
    @Override
    public SubHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
      View view = layoutInflater.inflate(R.layout.item_scroll_header, parent, false);
      return new SubHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubHolder holder, int position) {
      holder.bind("Button " + position);
    }

    @Override
    public int getItemCount() {
      return 20;
    }
  }

  static class SubHolder extends BaseHolder {

    private Button mButton;

    SubHolder(View itemView) {
      super(itemView);
      mButton = itemView.findViewById(R.id.button);
      mButton.setLayoutParams(
          new ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    void bind(String text) {
      mButton.setText(text);
    }
  }
}
