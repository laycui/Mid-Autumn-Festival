package com.leicui.leicui.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyModule {

    @Provides
    @Singleton
    public static MyExample provideMyExample() {
        return new MyExampleImpl();
    }

}
