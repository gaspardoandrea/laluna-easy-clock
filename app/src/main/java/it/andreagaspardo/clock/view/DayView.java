package it.andreagaspardo.clock.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import it.andreagaspardo.clock.R;
import it.andreagaspardo.clock.model.Helper;
import it.andreagaspardo.clock.model.HourModel;

public class DayView extends GridLayout {
    public static final int ICON_SIZE = 110;
    private int row = 0;
    private SharedPreferences preferences;
    private boolean added = false;
    private final Map<Integer, List<View>> hourElements = new HashMap<>();
    private HourModel hourModel;
    private Timer timer = new Timer();

    public DayView(Context context) {
        super(context);
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Add children.
     */
    public void addChildren() {
        if (added) {
            return;
        }
        preferences = getContext()
                .getSharedPreferences(getContext().getPackageName() + "_preferences", Context.MODE_PRIVATE);
        addMorningComponents();
        addAfternoonComponents();
        addEveningComponents();
        addNightComponents();
        initTimer();
        added = true;
    }

    private void initTimer() {
        hourModel = new HourModel(Helper.getCurrentLocale(getContext()));
        updateHour();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateHour();
            }
        }, 30000);
    }

    private void updateHour() {
        int start = getHour("wake_up", 7);
        hourModel.reset();
        for (Map.Entry<Integer, List<View>> entry : hourElements.entrySet()) {
            float f = 1;
            int k = entry.getKey();
            int currentHour = hourModel.getHour();
            if (currentHour < start) {
                currentHour += 24;
            }
            if (k > currentHour) {
                f = (float) 0.2;
            }
            int bg;
            boolean current = false;
            if (k == currentHour) {
                bg = getResources().getColor(R.color.primary, getContext().getTheme());
                current = true;
            } else {
                bg = getResources().getColor(R.color.bg, getContext().getTheme());
            }
            for (View v : entry.getValue()) {
                v.setAlpha(f);
                v.setBackgroundColor(bg);
                if (v instanceof TextView) {
                    ((TextView) v).setTypeface(null,
                            current ? Typeface.BOLD : Typeface.NORMAL);
                }
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addChildren();
    }

    private void addMorningComponents() {
        addRow(getHour("wake_up", 7),
                getHour("launch", 12),
                ResourcesCompat.getDrawable(getResources(), R.drawable.wake_up, getContext().getTheme()),
                ResourcesCompat.getDrawable(getResources(), R.drawable.launch, getContext().getTheme()));
    }

    private void addAfternoonComponents() {
        addRow(getHour("launch", 12) + 1,
                getHour("dinner", 19),
                ResourcesCompat.getDrawable(getResources(), R.drawable.rest1, getContext().getTheme()),
                ResourcesCompat.getDrawable(getResources(), R.drawable.dinner, getContext().getTheme()));
    }

    private void addEveningComponents() {
        addRow(getHour("dinner", 19) + 1,
                getHour("bedtime", 21),
                ResourcesCompat.getDrawable(getResources(), R.drawable.film, getContext().getTheme()),
                ResourcesCompat.getDrawable(getResources(), R.drawable.sleep, getContext().getTheme()));
    }

    private void addNightComponents() {
        addRow(getHour("bedtime", 21) + 1,
                getHour("wake_up", 7) - 1,
                ResourcesCompat.getDrawable(getResources(), R.drawable.night, getContext().getTheme()),
                ResourcesCompat.getDrawable(getResources(), R.drawable.alba2, getContext().getTheme()));
    }


    private void addRow(int start, int end, Drawable startIcon, Drawable endIcon) {
        if (startIcon != null) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(row), GridLayout.spec(0));
            params.width = ICON_SIZE;
            params.height = ICON_SIZE;
            ImageView startImg = new ImageView(getContext());
            startImg.setImageDrawable(startIcon);
            addView(startImg, params);
            addHourElement(startImg, start);
        }
        int col = 1;
        if (end < start) {
            end += 24;
        }
        for (int i = start; i <= end; i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(row), GridLayout.spec(col));
            params.width = ICON_SIZE;
            params.height = ICON_SIZE;
            TextView hour = new TextView(getContext());
            hour.setText(String.valueOf(i % 24));
            hour.setTextColor(getResources().getColor(R.color.text_bounds_color,
                    getContext().getTheme()));
            hour.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            hour.setPadding(0, 25, 0, 0);
            addView(hour, params);
            addHourElement(hour, i);
            col++;
            if (col > 8) {
                col = 0;
                row++;
            }
        }
        if (endIcon != null) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(row), GridLayout.spec(col));
            params.width = ICON_SIZE;
            params.height = ICON_SIZE;
            ImageView endImg = new ImageView(getContext());
            endImg.setImageDrawable(endIcon);
            addView(endImg, params);
            addHourElement(endImg, end);
        }

        row++;
    }

    private void addHourElement(View view, int h) {
        if (!hourElements.containsKey(h)) {
            hourElements.put(h, new ArrayList<>());
        }
        Objects.requireNonNull(hourElements.get(h)).add(view);
    }

    /**
     * Get hour from settings.
     *
     * @param settingsKey  String
     * @param defaultValue int
     * @return int
     */
    private int getHour(String settingsKey, int defaultValue) {
//        return defaultValue;
        return Integer.parseInt(preferences.getString(settingsKey, String.valueOf(defaultValue)));
    }
}
