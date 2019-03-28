package io.phatcat.mana.viewmodel;

import android.content.Context;

import com.jraska.livedata.TestObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import io.phatcat.mana.AppComponent;
import io.phatcat.mana.DaggerAppComponent;
import io.phatcat.mana.model.RecipeData;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4ClassRunner.class)
public class RecipeViewModelTest {

    @Rule public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();
    private RecipeViewModel subject;

    @Before
    public void setUp() {
        Context context = getInstrumentation().getTargetContext();
        AppComponent appComponent = DaggerAppComponent.builder().context(context).build();
        subject = new RecipeViewModel(appComponent.recipeRepository(), appComponent.recipeNetworkService());
    }

    @Test
    public void loadRecipesSmokeTest() throws InterruptedException {
        subject.loadRecipes(null);
        LiveData<List<RecipeData>> liveData = subject.getAllRecipes();
        TestObserver.test(liveData)
                .awaitValue()
                .assertHasValue()
                .assertHistorySize(1);
        List<RecipeData> recipeData = liveData.getValue();
        assertNotNull(recipeData);
    }
}
