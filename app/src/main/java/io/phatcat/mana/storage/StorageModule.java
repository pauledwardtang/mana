package io.phatcat.mana.storage;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import io.phatcat.mana.storage.local.RecipeDao;
import io.phatcat.mana.storage.local.RecipeDatabase;
import io.phatcat.mana.storage.local.RecipeLocalDataSource;

// TODO encapsulate data sources
@Module
public class StorageModule {
    @Provides
    @Singleton
    RecipeDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, RecipeDatabase.class, "recipes").build();
    }

    @Provides
    @Singleton
    RecipeDao provideRecipeDao(RecipeDatabase recipeDatabase) {
        return recipeDatabase.recipeDao();
    }

    @Provides
    @Singleton
    @Named("local")
    RecipeDataSource provideLocalDataSource(RecipeDao dao) {
        return new RecipeLocalDataSource(dao);
    }

    @Provides
    @Singleton
    RecipeRepository provideRecipeRepository(@Named("local") RecipeDataSource localDataSource) {
        return new RecipeRepository(localDataSource);
    }
}
