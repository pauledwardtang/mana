package io.phatcat.mana;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import io.phatcat.mana.concurrent.ConcurrencyModule;
import io.phatcat.mana.network.NetworkModule;
import io.phatcat.mana.network.RecipeNetworkService;
import io.phatcat.mana.storage.StorageModule;
import io.phatcat.mana.view.RecipesFragment;
import io.phatcat.mana.viewmodel.RecipeViewModel;
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
    void inject(RecipeNetworkService networkService);
}
