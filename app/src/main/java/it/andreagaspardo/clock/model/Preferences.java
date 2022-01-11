package it.andreagaspardo.clock.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import it.andreagaspardo.clock.R;

public class Preferences {
    private final Resources resources;
    private final Resources.Theme theme;
    private SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        resources = context.getResources();
        theme = context.getTheme();
    }

    public boolean showMinutesComponent() {
        return sharedPreferences.getBoolean("show_minutes_component", true);
    }

    public boolean showQuadrants() {
        return sharedPreferences.getBoolean("show_quadrants", true);
    }

    public boolean showMinutes() {
        return sharedPreferences.getBoolean("show_minutes", true);
    }

    public boolean showMinutesNumber() {
        return sharedPreferences.getBoolean("show_minutes_number", true);
    }

    public boolean uppercase() {
        return sharedPreferences.getBoolean("uppercase", true);
    }

    public boolean isDayStructureByFour() {
        return sharedPreferences.getString("day_structure_type", "1").equals("1");
    }

    /**
     * Get hour from settings.
     *
     * @param settingsKey  String
     * @param defaultValue int
     * @return int
     */
    public int getHour(String settingsKey, int defaultValue) {
        return Integer.parseInt(sharedPreferences.getString(settingsKey, String.valueOf(defaultValue)));
    }


    public int getLaunchTime() {
        return getHour("launch", 12);
    }

    public int getBedTime() {
        return getHour("bedtime", 21);
    }

    public int getDinnerTime() {
        return getHour("dinner", 19);
    }

    public int getWakeUpTime() {
        return getHour("wake_up", 7);
    }

    public Drawable getLaunchIcon() {
        return ResourcesCompat.getDrawable(resources, R.drawable.launch, theme);
    }

    public Drawable getWakeUpIcon() {
        return ResourcesCompat.getDrawable(resources, R.drawable.wake_up, theme);
    }

    public Drawable getRestIcon() {
        return ResourcesCompat.getDrawable(resources, R.drawable.rest1, theme);
    }

    public Drawable getDinnerIcon() {
        return ResourcesCompat.getDrawable(resources, R.drawable.dinner, theme);
    }

    public Drawable getSleepIcon() {
        return ResourcesCompat.getDrawable(resources, R.drawable.sleep, theme);
    }

    public Drawable getNightIcon() {
        return ResourcesCompat.getDrawable(resources, R.drawable.night, theme);
    }

    public Drawable getEveningIcon() {
        return ResourcesCompat.getDrawable(resources, R.drawable.film, theme);
    }

    public Drawable getSunriseIcon() {
        return ResourcesCompat.getDrawable(resources, R.drawable.alba2, theme);
    }
}
