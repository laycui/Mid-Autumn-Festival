package com.leicui.leicui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class MyDialogFragment extends DialogFragment {
  private static final String ARG_TITLE = "title";
  private static final String ARG_POSITIVE = "positive";

  public static MyDialogFragment newInstance(String title, String positive) {
    Bundle args = new Bundle();
    args.putString(ARG_TITLE, title);
    args.putString(ARG_POSITIVE, positive);

    MyDialogFragment fragment = new MyDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Bundle args = getArguments();
    String title = args.getString(ARG_TITLE);
    String positive = args.getString(ARG_POSITIVE);

    return new AlertDialog.Builder(getActivity())
        .setTitle(title)
        .setPositiveButton(
            positive,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {}
            })
        .create();
  }
}
