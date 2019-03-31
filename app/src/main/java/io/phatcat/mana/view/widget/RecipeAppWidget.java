package io.phatcat.mana.view.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.text.TextUtils;
import android.widget.RemoteViews;

import java.util.Arrays;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.core.text.TextUtilsCompat;
import io.phatcat.mana.R;
import io.phatcat.mana.model.RecipeData;
import io.phatcat.mana.utils.WidgetUtils;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeAppWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String title, String ingredients) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);

        views.setTextViewText(R.id.title, title);
        views.setTextViewText(R.id.description, ingredients);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetUtils.startWidgetUpdate(context, null);
    }

    @Override
    public void onEnabled(Context context) {
        WidgetUtils.startWidgetUpdate(context, null);
    }
}
