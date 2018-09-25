package com.leicui.leicui.dagger;

import java.util.Date;

public class MyExampleImpl implements MyExample {

    private long mDate;

    public MyExampleImpl() {
        mDate = new Date().getTime();
    }

    @Override
    public long getDate() {
        return mDate;
    }
}
