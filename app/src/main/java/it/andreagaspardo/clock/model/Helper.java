package it.andreagaspardo.clock.model;

import android.content.Context;

import java.util.Locale;

/**
 * Helper class
 */
public class Helper {
    public static Locale getCurrentLocale(Context context) {
        return context.getResources().getConfiguration().getLocales().get(0);
    }
}
