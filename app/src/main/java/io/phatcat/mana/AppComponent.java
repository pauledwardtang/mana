package io.phatcat.mana;

import android.content.Context;

import javax.inject.Singleton;

import androidx.annotation.VisibleForTesting;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import io.phatcat.mana.concurrent.ConcurrencyModule;
import io.phatcat.mana.network.NetworkModule;
import io.phatcat.mana.network.RecipeNetworkService;
import io.phatcat.mana.storage.RecipeRepository;
import io.phatcat.mana.storage.StorageModule;
import io.phatcat.mana.viewmodel.ViewModelModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        StorageModule.class,
        ConcurrencyModule.class,
        NetworkModule.class,
        ViewModelModule.class
})
public interface AppComponent extends AndroidInjector<ManaApplication> {
    @VisibleForTesting RecipeRepository recipeRepository();
    @VisibleForTesting RecipeNetworkService recipeNetworkService();

    @Component.Builder
    interface Builder {
        @BindsInstance Builder context(Context context);
        AppComponent build();
    }
}
