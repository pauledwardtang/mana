package io.phatcat.mana.network;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Definitely could use BuildConfig consts in this class :)
 */
@Module
public class NetworkModule {
    @Provides
    @Named("shouldEnqueue")
    Boolean provideShouldEnqueue() {
        return true;
    }

    @Provides
    @Named("baseUrl")
    String provideBaseUrl() {
        return "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(@Named("baseUrl") String baseUrl) {
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

}
