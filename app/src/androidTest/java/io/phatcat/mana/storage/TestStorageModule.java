package io.phatcat.mana.storage;

import android.content.Context;

import androidx.room.Room;
import io.phatcat.mana.storage.local.RecipeDatabase;

/**
 * Wrapper class to expose storage module
 */
public class TestStorageModule extends StorageModule {
    // TODO verify in memory DB is actually used?
    @Override
    RecipeDatabase provideDatabase(Context context) {
        return Room.inMemoryDatabaseBuilder(context, RecipeDatabase.class).build();
    }
}
