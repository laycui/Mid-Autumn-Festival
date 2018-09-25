package com.leicui.leicui;

import android.app.Application;

import com.leicui.leicui.dagger.DaggerMyComponent;
import com.leicui.leicui.dagger.MyComponent;
import com.leicui.leicui.dagger.MyModule;

public class MyApplication extends Application {

    private MyComponent mMyComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mMyComponent = DaggerMyComponent.builder()
                                        .myModule(new MyModule())
                                        .build();
    }

    MyComponent getMyComponent() {
        return mMyComponent;
    }
}
