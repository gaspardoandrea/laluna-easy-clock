package it.andreagaspardo.clock.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import it.andreagaspardo.clock.model.Helper;
import it.andreagaspardo.clock.model.HourModel;
import it.andreagaspardo.clock.model.Preferences;

public class DayView extends GridLayout {
    public static final int ICON_SIZE = 110;
    private int row = 0;
    private Preferences preferences;
    private boolean added = false;
    private final Map<Integer, List<DayViewComponent>> hourElements = new HashMap<>();
    private HourModel hourModel;
    private final Timer timer = new Timer();

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
        preferences = new Preferences(getContext());
        if (preferences.isDayStructureByFour()) {
            addAllComponents();
        } else {
            addMorningComponents();
            addAfternoonComponents();
            addEveningComponents();
            addNightComponents();
        }
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
        int start = preferences.getWakeUpTime();
        hourModel.reset();
        for (Map.Entry<Integer, List<DayViewComponent>> entry : hourElements.entrySet()) {
            int k = entry.getKey();
            int now = hourModel.getHour();
            if (now < start) {
                now += 24;
            }
            boolean isDone = k <= now;
            boolean current = k == now;
            for (DayViewComponent v : entry.getValue()) {
                v.updateStatus(isDone, current);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addChildren();
    }

    private void addMorningComponents() {
        addRow(preferences.getWakeUpTime(),
                preferences.getLaunchTime(),
                preferences.getWakeUpIcon(),
                preferences.getLaunchIcon());
    }

    private void addAfternoonComponents() {
        addRow(preferences.getLaunchTime() + 1,
                preferences.getDinnerTime(),
                preferences.getRestIcon(),
                preferences.getDinnerIcon());
    }

    private void addEveningComponents() {
        addRow(preferences.getDinnerTime() + 1,
                preferences.getBedTime(),
                preferences.getEveningIcon(),
                preferences.getSleepIcon());
    }

    private void addNightComponents() {
        addRow(preferences.getBedTime() + 1,
                preferences.getWakeUpTime() - 1,
                preferences.getNightIcon(),
                preferences.getSunriseIcon());
    }

    private void addAllComponents() {
        int col = 0;
        row = 1;
        for (int hour = 0; hour < 24; hour++) {
            LayoutParams params = createLayoutParams(col);
            HourText hourText = new HourText(getContext());
            hourText.updateText(hour);
            addView(hourText, params);
            addHourElement(hourText, hour);
            if (hour == preferences.getWakeUpTime()) {
                addIconOverText(col, hour, preferences.getWakeUpIcon());
            } else if (hour == preferences.getLaunchTime()) {
                addIconOverText(col, hour, preferences.getLaunchIcon());
            } else if (hour == preferences.getDinnerTime()) {
                addIconOverText(col, hour, preferences.getDinnerIcon());
            } else if (hour == preferences.getBedTime()) {
                addIconOverText(col, hour, preferences.getSleepIcon());
            }
            col++;
            if (col > 8) {
                row += 2;
                col = 0;
            }
        }
    }

    private void addIconOverText(int col, int hour, Drawable icon) {
        row--;
        HourIcon img = new HourIcon(getContext());
        img.setImageDrawable(icon);
        LayoutParams imgParams = createLayoutParams(col);
        addView(img, imgParams);
        addHourElement(img, hour);
        row++;
    }


    private void addRow(int start, int end, Drawable startIcon, Drawable endIcon) {
        if (startIcon != null) {
            HourIcon startImg = new HourIcon(getContext());
            startImg.setImageDrawable(startIcon);
            LayoutParams params = createLayoutParams(0);
            addView(startImg, params);
            addHourElement(startImg, start);
        }

        int col = 1;
        if (end < start) {
            end += 24;
        }
        for (int i = start; i <= end; i++) {
            LayoutParams params = createLayoutParams(col);
            HourText hour = new HourText(getContext());
            hour.updateText(i);
            addView(hour, params);
            addHourElement(hour, i);
            col++;
            if (col > 8) {
                col = 0;
                row++;
            }
        }
        if (endIcon != null) {
            LayoutParams params = createLayoutParams(col);
            HourIcon endImg = new HourIcon(getContext());
            endImg.setImageDrawable(endIcon);
            addView(endImg, params);
            addHourElement(endImg, end);
        }

        row++;
    }

    @NonNull
    private LayoutParams createLayoutParams(int column) {
        LayoutParams params = new LayoutParams(
                GridLayout.spec(row), GridLayout.spec(column));
        params.width = getIconSize();
        params.height = getIconSize();
        return params;
    }

    private int getIconSize() {
        // TODO
//        if (preferences.isDayStructureByFour()) {
//            return getWidth() / 6;
//        } else {
        return ICON_SIZE;
//        }
    }

    private void addHourElement(DayViewComponent view, int h) {
        if (!hourElements.containsKey(h)) {
            hourElements.put(h, new ArrayList<>());
        }
        Objects.requireNonNull(hourElements.get(h)).add(view);
    }
}
