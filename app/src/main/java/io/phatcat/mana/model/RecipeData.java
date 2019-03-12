package io.phatcat.mana.model;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class RecipeData {
    @Embedded
    public Recipe recipe;

    @Relation(parentColumn = "id", entityColumn = "recipeId", entity=RecipeIngredients.class)
    public List<RecipeIngredientsData> recipeIngredients = new ArrayList<>();

    @Relation(parentColumn = "id", entityColumn = "recipeId", entity = Step.class)
    public List<Step> recipeSteps = new ArrayList<>();

}
