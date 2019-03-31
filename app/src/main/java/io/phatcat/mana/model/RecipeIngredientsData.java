package io.phatcat.mana.model;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

public class RecipeIngredientsData {
    @Embedded public RecipeIngredients recipeIngredients;

    // These are unique, so it'll always have 1 element in it. Cascading FK would delete this
    // element so even 0 is impossible.
    @Relation(parentColumn="ingredientId", entityColumn="id", entity=Ingredient.class)
    public Set<Ingredient> ingredients = new HashSet<>();

    @Relation(parentColumn="measureId", entityColumn="id", entity=Measure.class)
    public Set<Measure> measures = new HashSet<>();

    public RecipeIngredientsData() {}

    public Ingredient getIngredient() {
        return (ingredients == null || ingredients.isEmpty()) ? null: ingredients.iterator().next();
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public Measure getMeasure() {
        return (measures == null || measures.isEmpty()) ? null: measures.iterator().next();
    }

    public void setMeasure(Measure measure) {
        this.measures.add(measure);
    }

    public double getQuantity() {
        return recipeIngredients.quantity;
    }

    public String getMeasureForDisplay() {
        String measureName = getMeasure().shortName;
        return getQuantity() + " " + (measureName.equals("UNIT") ? "" : measureName);
    }

    @NonNull
    @Override
    public String toString() {
        return getIngredient().name + " (" + getMeasureForDisplay().toLowerCase().trim() + ")";
    }
}
