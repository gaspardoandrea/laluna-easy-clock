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
    public static final int ICON_SIZE = 120;
    private Preferences preferences;
    private boolean added = false;
    private final Map<Integer, List<DayViewComponent>> hourElements = new HashMap<>();
    private final List<DayRowLayout> rows = new ArrayList<>();
    private HourModel hourModel;
    private final Timer timer = new Timer(true);
    private Integer currentHour = null;

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
        addComponents();
        android.view.ViewGroup.LayoutParams params = getLayoutParams();
        setLayoutParams(params);
        hourModel = new HourModel(Helper.getCurrentLocale(getContext()));
        initTimer();
        added = true;
    }

    private void initTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                post(() -> updateHour());
            }
        }, 0, 1000);
    }

    private void updateHour() {
        hourModel.reset();
        int now = hourModel.getHour();

        if (currentHour == null || !currentHour.equals(now)) {
            for (Map.Entry<Integer, List<DayViewComponent>> entry : hourElements.entrySet()) {
                int k = entry.getKey();
                boolean isDone = k <= now;
                boolean current = k == now;
                for (DayViewComponent v : entry.getValue()) {
                    v.updateStatus(isDone, current);
                }
            }
            for (DayRowLayout row : rows) {
                row.setCurrentHour(now);
            }
        }
        currentHour = now;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addChildren();
    }

    private void addComponents() {
        int col = 0;
        int row = 0;
        DayRowLayout current = null;
        for (int hour = 0; hour < 24; hour++) {
            if (hour % 6 == 0) {
                current = new DayRowLayout(getContext());
                current.setInterval(hour, hour + 5);
                LayoutParams params = new LayoutParams(
                        GridLayout.spec(row++), GridLayout.spec(0));
                addView(current, params);
                rows.add(current);
                col = 0;
            }
            if (hour == 0) {
                addIconBeforeLine(current, col++, hour, preferences.getD1());
                col++;
            } else if (hour == 6) {
                addIconBeforeLine(current, col++, hour, preferences.getD2());
                col++;
            } else if (hour == 12) {
                addIconBeforeLine(current, col++, hour, preferences.getD3());
                col++;
            } else if (hour == 18) {
                addIconBeforeLine(current, col++, hour, preferences.getD4());
                col++;
            }
            LayoutParams params = createLayoutParams(col++);
            params.setMargins(4, 8, 4, 8);
            params.width -= 8;
            params.height -= 16;

            HourText hourText = new HourText(getContext());
            hourText.updateText(hour);
            current.addView(hourText, params);
            addHourElement(hourText, hour);
        }
    }

    private void addIconBeforeLine(GridLayout view, int col, int hour, Drawable icon) {
        HourIcon img = new HourIcon(getContext());
        img.setImageDrawable(icon);
        LayoutParams imgParams = createLayoutParams(col);
        view.addView(img, imgParams);
        addHourElement(img, hour);
    }

    @NonNull
    private LayoutParams createLayoutParams(int column) {
        LayoutParams params = new LayoutParams(
                GridLayout.spec(0), GridLayout.spec(column));
        params.width = getIconSize();
        params.height = getIconSize();
        return params;
    }

    private int getIconSize() {
        return ICON_SIZE;
    }

    private void addHourElement(DayViewComponent view, int h) {
        if (!hourElements.containsKey(h)) {
            hourElements.put(h, new ArrayList<>());
        }
        Objects.requireNonNull(hourElements.get(h)).add(view);
    }
}
