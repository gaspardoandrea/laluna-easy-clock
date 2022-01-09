package it.andreagaspardo.clock.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import it.andreagaspardo.clock.R;

public class DayView extends GridLayout {
    public static final int ICON_SIZE = 100;
    private int row = 0;
    private SharedPreferences preferences;
    private boolean added = false;

    public DayView(Context context) {
        super(context);
//        addChildren();
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        addChildren();
    }

    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        addChildren();
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
        added = true;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            addChildren();
        }

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
                null,
                ResourcesCompat.getDrawable(getResources(), R.drawable.sleep, getContext().getTheme()));
    }

    private void addNightComponents() {
        addRow(getHour("bedtime", 21) + 1,
                getHour("wake_up", 7) - 1,
                ResourcesCompat.getDrawable(getResources(), R.drawable.night, getContext().getTheme()),
                null);
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
        }
        int col = 1;
        for (int i = start; i <= end; i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(row), GridLayout.spec(col));
            params.width = ICON_SIZE;
            params.height = ICON_SIZE;
            TextView hour = new TextView(getContext());
            hour.setText(String.valueOf(i));
            hour.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            hour.setPadding(0, 25, 0, 0);
            addView(hour, params);
            col++;
        }
        if (endIcon != null) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(row), GridLayout.spec(col));
            params.width = ICON_SIZE;
            params.height = ICON_SIZE;
            ImageView endImg = new ImageView(getContext());
            endImg.setImageDrawable(endIcon);
            addView(endImg, params);
        }

        row++;
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
