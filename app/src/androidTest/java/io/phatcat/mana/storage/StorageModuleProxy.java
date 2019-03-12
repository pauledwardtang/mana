package io.phatcat.mana.storage;

import android.content.Context;

import androidx.room.Room;
import io.phatcat.mana.storage.local.RecipeDatabase;

/**
 * Wrapper class to expose storage module
 */
public class StorageModuleProxy {
    private StorageModule storageModule;

    public StorageModuleProxy(StorageModule module) {
        storageModule = module;
    }

    public RecipeRepository provideRecipeRepository() {
        Context context = storageModule.provideContext();
        RecipeDatabase database = Room.inMemoryDatabaseBuilder(context, RecipeDatabase.class).build();
        return storageModule.provideRecipeRepository(storageModule.provideLocalDataSource(database.recipeDao()));
    }
}
