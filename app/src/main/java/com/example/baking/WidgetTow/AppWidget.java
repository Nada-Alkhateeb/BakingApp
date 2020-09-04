package com.example.baking.WidgetTow;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.PeriodicSync;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.baking.R;
import com.example.baking.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class AppWidget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds){

        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_tow);

            SharedPreferences sharedPreferences =context.getSharedPreferences("shard preferences",Context.MODE_PRIVATE);
            Gson gson=new Gson();
            String json=sharedPreferences.getString("Favorite Recipe",null);
            Type type=new TypeToken<Recipe>() {}.getType();
            Recipe FRecipe=gson.fromJson(json,type);

        if (FRecipe != null && !FRecipe.getName().isEmpty()){
            updateViews.setTextViewText(R.id.recipe_name, FRecipe.getName());
            updateViews.setTextViewText(R.id.widget_listView, FRecipe.AllIngredient());
            } else {
            updateViews.setTextViewText(R.id.recipe_name,context.getString(R.string.recipe));
            updateViews.setTextViewText(R.id.widget_listView,context.getString(R.string.widget_msg));
        }


        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        ComponentName cn = new ComponentName(context, AppWidget.class);
        mgr.updateAppWidget(cn, updateViews);

        }

    }
}
