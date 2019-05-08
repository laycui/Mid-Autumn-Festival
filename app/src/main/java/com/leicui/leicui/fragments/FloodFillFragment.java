package com.leicui.leicui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leicui.leicui.R;

public class FloodFillFragment extends Fragment {

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
    MyAdapter myAdapter = new MyAdapter();

    recyclerView.setAdapter(myAdapter);
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 20));
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.activity_flood_fill, container, false);
  }

  private static class MyAdapter extends RecyclerView.Adapter<GridViewHolder> {

    private boolean[][] mGrid;

    MyAdapter() {
      mGrid = new boolean[20][20];

      for (int i = 0; i < 20; i++) {
        mGrid[i][9] = true;
      }
      for (int j = 0; j < 20; j++) {
        mGrid[9][j] = true;
      }
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
      View view = layoutInflater.inflate(R.layout.flood_item, parent, false);
      return new GridViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
      int row = position / 20;
      int col = position % 20;
      holder.bindView(mGrid[row][col]);
    }

    @Override
    public int getItemCount() {
      return 20 * 20;
    }

    void update(int position) {
      int row = position / 20;
      int col = position % 20;
      floodFill(row, col);

      //      notifyDataSetChanged();
    }

    private void floodFill(int row, int col) {
      boolean[][] visited = new boolean[20][20];
      if (!mGrid[row][col]) {
        floodFillHelper(row, col, visited);
      }
    }

    private void floodFillHelper(int i, int j, boolean[][] visited) {
      if (i < 0 || i == 20 || j < 0 || j == 20 || visited[i][j] || mGrid[i][j]) {
        return;
      }
      mGrid[i][j] = true;
      visited[i][j] = true;

      notifyItemChanged(i * 20 + j);                                /// !!!

      floodFillHelper(i - 1, j, visited);
      floodFillHelper(i + 1, j, visited);
      floodFillHelper(i, j - 1, visited);
      floodFillHelper(i, j + 1, visited);
    }
  }

  private static class GridViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {

    private MyAdapter mMyAdapter;
    private View mCell;

    GridViewHolder(View view, MyAdapter myAdapter) {
      super(view);

      mCell = itemView.findViewById(R.id.cell);
      mCell.setOnClickListener(this);
      mMyAdapter = myAdapter;
    }

    void bindView(boolean filled) {
      if (filled) {
        mCell.setBackgroundColor(Color.RED);
      }
    }

    @Override
    public void onClick(View v) {
      int position = getAdapterPosition();                           // !!!!
      mMyAdapter.update(position);
    }
  }
}
