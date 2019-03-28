package io.phatcat.mana.storage.local;

import android.os.AsyncTask;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.storage.RecipeDataSource;

public class RecipeLocalDataSource implements RecipeDataSource {
    private RecipeDao dao;

    @Inject
    public RecipeLocalDataSource(RecipeDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<RecipeData>> getAllRecipeData() {
        return dao.getAllRecipeData();
    }

    @Override
    public LiveData<RecipeData> getRecipeData(long recipeId) {
        return dao.getRecipeData(recipeId);
    }

    @Override
    public void addRecipeData(List<RecipeData> recipeData) {
        new DaoTask<List<RecipeData>>(dao, RecipeDao::insert).execute(recipeData);
    }

    /**
     * Interface for simplified async Dao interactions.
     * @param <Subject>
     */
    private interface DaoInteractor<Subject> {
        void interact(RecipeDao dao, Subject subject);
    }

    /**
     * Async task for interacting with the RecipeDao.
     * @param <Params>
     */
    private static class DaoTask<Params> extends AsyncTask<Params, Void, Void> {
        private RecipeDao recipeDao;
        private DaoInteractor<Params> daoInteractor;

        /**
         * @param dao The Dao to interact with
         * @param interactor Simple interface for allowing inline lambdas
         */
        DaoTask(RecipeDao dao, DaoInteractor<Params> interactor) {
            recipeDao = dao;
            this.daoInteractor = interactor;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final Params... params) {
            daoInteractor.interact(recipeDao, params[0]);
            return null;
        }
    }
}
