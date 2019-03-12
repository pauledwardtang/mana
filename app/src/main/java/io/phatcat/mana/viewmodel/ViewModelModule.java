package io.phatcat.mana.viewmodel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.lifecycle.ViewModel;
import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import io.phatcat.mana.network.RecipeNetworkService;
import io.phatcat.mana.storage.RecipeRepository;

@Module
// Guidance via: https://www.techyourchance.com/dependency-injection-viewmodel-with-dagger-2/
public class ViewModelModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }

    @Provides
    @IntoMap
    @ViewModelKey(RecipeViewModel.class)
    ViewModel recipeViewModel(RecipeRepository recipeRepository, RecipeNetworkService networkService) {
        return new RecipeViewModel(recipeRepository, networkService);
    }
}