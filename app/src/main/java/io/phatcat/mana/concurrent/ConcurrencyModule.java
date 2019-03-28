package io.phatcat.mana.concurrent;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ConcurrencyModule {
    @Provides
    @Singleton
    public AppExecutors provideExecutors() {
        return new AppExecutors(
                new MainThreadExecutor(),
                Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(3)
        );
    }
    private class MainThreadExecutor implements Executor {
        private final Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mHandler.post(command);
        }
    }
}
