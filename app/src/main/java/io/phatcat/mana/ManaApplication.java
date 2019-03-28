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

    protected AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        String baseUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
        appComponent = DaggerAppComponent.builder()
                .context(this)
                .networkModule(new NetworkModule(baseUrl, true))
                .build();
        appComponent.inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

}
