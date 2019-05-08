package com.leicui.leicui.dagger;

import com.leicui.leicui.fragments.MainFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MyModule.class)
public interface MyComponent {

  void inject(MainFragment mainFragment);
}
