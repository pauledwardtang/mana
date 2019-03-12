package io.phatcat.mana.storage.local;

import android.content.Context;

import com.jraska.livedata.TestObserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import io.phatcat.mana.model.Ingredient;
import io.phatcat.mana.model.Measure;
import io.phatcat.mana.model.Recipe;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.model.RecipeIngredients;
import io.phatcat.mana.model.RecipeIngredientsData;
import io.phatcat.mana.model.Step;

import static io.phatcat.mana.storage.local.TestUtil.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4ClassRunner.class)
public class RecipeDaoTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();
    private RecipeDao dao;
    private RecipeDatabase db;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, RecipeDatabase.class).build();
        dao = db.recipeDao();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void databaseSetupTest() {
        assertNotNull(db);
    }

    @Test
    public void insertRecipeTest() throws InterruptedException {
        Recipe recipe = new Recipe(1, "Brownies", 2.0, null);
        dao.insert(recipe);
        LiveData<List<Recipe>> recipesLiveData = dao.getAllRecipes();
        TestObserver.test(recipesLiveData)
                .awaitValue()
                .assertHasValue();
    }

    @Test
    /*
     * Test with no IDs known. Check generated IDs all match up correctly
     */
    public void insertNewRecipeDataTest() throws InterruptedException {
        // Setup - Set all IDs to 0 (not in DB).
        // Create Recipe
        Recipe recipe = new Recipe(1L, "Bread", 1.0, "imageUrl");

        Step step = createStep(0);
        Ingredient ingredient = createIngredient();
        ingredient.id = 0;

        Measure measure = createMeasure();
        measure.id = 0;

        RecipeIngredientsData ingredientsData = new RecipeIngredientsData();
        ingredientsData.setIngredient(ingredient);
        ingredientsData.setMeasure(measure);
        ingredientsData.recipeIngredients = new RecipeIngredients(2);

        RecipeData data = createRecipeData(recipe, ingredientsData, step);

        // Action
        dao.insert(Collections.singletonList(data));

        // Verify
        LiveData<List<RecipeData>> recipeDataList = dao.getAllRecipeData();
        TestObserver.test(recipeDataList)
                .awaitValue()
                .assertHasValue()
                .assertHistorySize(1);

        assertNotNull(recipeDataList.getValue());
        RecipeData resultData = recipeDataList.getValue().get(0);
        assertNotNull(resultData.recipeIngredients);
        assertEquals(1, resultData.recipeIngredients.size());

        RecipeIngredientsData resultIngredients = resultData.recipeIngredients.get(0);
        assertNotNull(resultIngredients.recipeIngredients);
        assertEquals(1, resultIngredients.recipeIngredients.recipeId);
        assertEquals(1, resultIngredients.recipeIngredients.ingredientId);
        assertEquals(1, resultIngredients.recipeIngredients.measureId);

        assertEquals(1, resultIngredients.ingredients.size());
        assertEquals(1, resultIngredients.measures.size());
        assertNotNull(resultIngredients.getIngredient());
        assertNotNull(resultIngredients.getMeasure());
        assertEquals(1, resultIngredients.getIngredient().id);
        assertEquals(ingredient.name, resultIngredients.getIngredient().name);
        assertEquals(1, resultIngredients.getMeasure().id);
        assertEquals(measure.shortName, resultIngredients.getMeasure().shortName);
        assertEquals(measure.fullName, resultIngredients.getMeasure().fullName);
    }

    @Test
    public void insertKnownDataRecipeDataTest() throws InterruptedException {
        // Setup
        RecipeData data = createRecipeData(1L);

        // Action
        dao.insert(Collections.singletonList(data));

        // Verification
        // Raw recipes table
        TestObserver.test(dao.getAllRecipes())
                .awaitValue()
                .assertHasValue()
                .assertHistorySize(1);

        // Underlying steps table
        TestObserver.test(dao.findStepsByRecipeId(data.recipe.id))
                .awaitValue()
                .assertHasValue()
                .assertHistorySize(1);

        // Test POJO composite
        LiveData<List<RecipeData>> recipeDataList = dao.getAllRecipeData();
        TestObserver.test(recipeDataList)
                .awaitValue()
                .assertHasValue()
                .assertHistorySize(1);
    }
}
