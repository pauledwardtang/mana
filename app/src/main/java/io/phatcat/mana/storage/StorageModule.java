package io.phatcat.mana.storage;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Binds;
import dagger.BindsInstance;
import dagger.Module;
import dagger.Provides;
import io.phatcat.mana.storage.local.RecipeDao;
import io.phatcat.mana.storage.local.RecipeDatabase;
import io.phatcat.mana.storage.local.RecipeLocalDataSource;

@Module
public abstract class StorageModule {
    @Provides
    @Singleton
    static RecipeDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, RecipeDatabase.class, "recipes").build();
    }

    @Provides
    @Singleton
    static RecipeDao provideRecipeDao(RecipeDatabase recipeDatabase) {
        return recipeDatabase.recipeDao();
    }

    // Using named here since a "remote" data source could be added in the future.
    @Binds
    @Named("local")
    abstract RecipeDataSource provideLocalDataSource(RecipeLocalDataSource localDataSource);
}
