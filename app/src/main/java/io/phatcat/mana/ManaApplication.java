package io.phatcat.mana;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import androidx.annotation.VisibleForTesting;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.phatcat.mana.concurrent.ConcurrencyModule;
import io.phatcat.mana.network.NetworkModule;
import io.phatcat.mana.storage.StorageModule;

public class ManaApplication extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent appComponent = DaggerAppComponent.builder().context(this).build();
        appComponent.inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

}
