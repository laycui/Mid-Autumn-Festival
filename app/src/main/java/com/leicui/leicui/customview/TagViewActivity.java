package com.leicui.leicui.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.leicui.leicui.R;
import com.leicui.leicui.fragments.MyDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class TagViewActivity extends AppCompatActivity {

    List<View> mTagList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        TagLayout tagLayout = findViewById(R.id.tagLayout);
        LayoutInflater layoutInflater = getLayoutInflater();
        String tag;
        for (int i = 0; i < 20; i++) {
            tag = "#tag" + i;
            View tagView = layoutInflater.inflate(R.layout.tag_item, null, false);

            TextView tagTextView = tagView.findViewById(R.id.tagTextView);
            tagTextView.setText(tag);
            tagLayout.addView(tagView);
            mTagList.add(tagView);
        }
        mTagList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogFragment myDialogFragment =
                        MyDialogFragment.newInstance("Title", "Positive Button");
                myDialogFragment.show(getFragmentManager(), "Tag");
            }
        });
    }
}
