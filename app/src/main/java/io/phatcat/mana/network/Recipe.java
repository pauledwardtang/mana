package io.phatcat.mana.network;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe POJO
 */
public class Recipe {
    public long id;
    public String name;
    public List<Ingredient> ingredients = new ArrayList<>();
    public List<Step> steps = new ArrayList<>();
    public long servings;
    public String image;

    public class Ingredient {
        public double quantity;
        public String measure;
        public String ingredient;
    }

    public class Step {
        public int id;
        public String shortDescription;
        public String description;
        public String videoURL;
        public String thumbnailURL;
    }
}
