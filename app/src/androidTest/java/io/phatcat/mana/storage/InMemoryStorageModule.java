package io.phatcat.mana.storage;

import android.content.Context;

import androidx.room.Room;
import io.phatcat.mana.storage.local.RecipeDatabase;
import io.phatcat.mana.storage.local.RecipeLocalDataSource;

/**
 * Unfortunately, defining a Module as abstract eliminates subclassing as an option for testing
 * since you must define instance methods as static, i.e. not overrideable. Another option is to
 * define a TestAppComponent that includes this module.
 *
 */
@Deprecated
public class InMemoryStorageModule { //extends StorageModule {
//    @Override
//    RecipeDatabase provideDatabase(Context context) {
//        return Room.inMemoryDatabaseBuilder(context, RecipeDatabase.class).build();
//    }
}
