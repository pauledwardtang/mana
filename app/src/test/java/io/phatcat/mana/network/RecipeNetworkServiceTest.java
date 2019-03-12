package io.phatcat.mana.network;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;

import io.phatcat.mana.AppComponent;
import io.phatcat.mana.DaggerAppComponent;
import io.phatcat.mana.storage.StorageModule;
import retrofit2.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RecipeNetworkServiceTest {

    @Test
    public void loadRecipesTest() throws IOException, NullPointerException {
        String baseUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
        AppComponent appComponent = DaggerAppComponent
                .builder()
                .storageModule(new StorageModule(null))
                .networkModule(new NetworkModule(baseUrl, false))
                .build();

        RecipeNetworkService service = new RecipeNetworkService(false);
        appComponent.inject(service);
        Response<List<Recipe>> response = service.loadRecipes();
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertThat(response.body().size(), is(4));
    }
}