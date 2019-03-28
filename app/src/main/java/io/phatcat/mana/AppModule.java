package io.phatcat.mana;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.phatcat.mana.view.MainActivity;
import io.phatcat.mana.view.RecipesFragment;
import io.phatcat.mana.view.details.RecipeDetailsActivity;
import io.phatcat.mana.view.details.RecipeDetailsFragment;
import io.phatcat.mana.view.guidedsteps.GuidedRecipeStepActivity;
import io.phatcat.mana.view.guidedsteps.GuidedStepDetailsFragment;
import io.phatcat.mana.view.guidedsteps.GuidedStepNavigationFragment;

@Module
public abstract class AppModule {
    // Activity Injectors
    @ContributesAndroidInjector abstract MainActivity mainActivityInjector();
    @ContributesAndroidInjector abstract RecipeDetailsActivity recipeDetailsActivityInjector();
    @ContributesAndroidInjector abstract GuidedRecipeStepActivity recipeGuideActivityInjector();


    // Fragment Injectors
    @ContributesAndroidInjector abstract RecipesFragment recipesFragmentInjector();
    @ContributesAndroidInjector abstract RecipeDetailsFragment recipeDetailsFragmentInjector();
    @ContributesAndroidInjector abstract GuidedStepNavigationFragment recipeGuideFragmentInjector();
    @ContributesAndroidInjector abstract GuidedStepDetailsFragment recipeGuideDetailsFragmentInjector();
}
