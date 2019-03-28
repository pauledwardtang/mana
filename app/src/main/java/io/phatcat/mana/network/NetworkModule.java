package io.phatcat.mana.network;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    private final String baseUrl;
    private final boolean shouldEnqueue;

    public NetworkModule(String baseUrl, boolean shouldEnqueue) {
        this.baseUrl = baseUrl;
        this.shouldEnqueue = shouldEnqueue;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    RecipeApi provideRecipeApi(Retrofit retrofit) {
        return retrofit.create(RecipeApi.class);
    }

    @Provides
    @Singleton
    RecipeNetworkService provideNetworkService(RecipeApi api){
        return new RecipeNetworkService(api, shouldEnqueue);
    }
}
