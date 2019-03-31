package io.phatcat.mana.utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import io.phatcat.mana.R;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.view.widget.RecipeAppWidget;

public class WidgetUtils {
    private static final String PREF_WIDGET_TITLE = "PREF_WIDGET_TITLE";
    private static final String PREF_WIDGET_DESCRIPTION = "PREF_WIDGET_DESCRIPTION";

    // Disallow instantiation
    private WidgetUtils() {}

    /**
     * Asynchronously updates all widgets if available
     * @param context Context to use for updating widgets
     * @param recipeData New data for widget update
     */
    public static void startWidgetUpdate(Context context, RecipeData recipeData) {
        Context ctx = context.getApplicationContext();
        AppWidgetManager manager = AppWidgetManager.getInstance(ctx);
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(ctx, RecipeAppWidget.class));

        String title, description;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (recipeData == null) {
            title = prefs.getString(PREF_WIDGET_TITLE, ctx.getString(R.string.recipe_not_found));
            description = prefs.getString(PREF_WIDGET_DESCRIPTION, "");
        }
        else {
            title = recipeData.recipe.name;
            description = TextUtils.join(",\n", recipeData.recipeIngredients);
            prefs.edit()
                    .putString(PREF_WIDGET_TITLE, title)
                    .putString(PREF_WIDGET_DESCRIPTION, description)
                    .apply();
        }

        int i = 0;
        for (; i < appWidgetIds.length; ++i) {
            RecipeAppWidget.updateAppWidget(ctx, manager, appWidgetIds[i], title, description);
        }
    }

}
