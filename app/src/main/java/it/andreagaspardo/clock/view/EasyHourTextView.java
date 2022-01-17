package it.andreagaspardo.clock.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import it.andreagaspardo.clock.model.Helper;
import it.andreagaspardo.clock.model.HourModel;
import it.andreagaspardo.clock.model.Preferences;

public class EasyHourTextView extends LinearLayout {
    private final Timer timer = new Timer(true);
    private HourModel hourModel;
    private String currentHour = null;
    private Preferences preferences;

    public EasyHourTextView(Context context) {
        super(context);
    }

    public EasyHourTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyHourTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        preferences = new Preferences(getContext());
        initTimer();
    }


    private void initTimer() {
        hourModel = new HourModel(Helper.getCurrentLocale(getContext()));
        initUppercase();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                post(() -> updateHour());
            }
        }, 0, 1000);
    }

    private void initUppercase() {
        Preferences preferences = new Preferences(getContext());
        if (preferences.uppercase()) {
            TextView tv = (TextView) getChildAt(1);
            tv.setText(tv.getText().toString().toUpperCase());
        }
    }

    public void updateHour() {
        hourModel.reset();
        String hourAsString = hourModel.getHourAsString();

        if (currentHour == null || !currentHour.equals(hourAsString)) {
            updateHourLabel(hourAsString);
            updateIcon(hourModel.getHour());
        }
        currentHour = hourAsString;
    }

    private void updateIcon(int hour) {
        ImageView img = (ImageView) getChildAt(0);
        img.setImageDrawable(getCurrentImageDrawable(hour));
    }

    private Drawable getCurrentImageDrawable(int hour) {
        if (hour < preferences.getWakeUpTime()) {
            return preferences.getSleepIcon();
        } else if (hour == preferences.getWakeUpTime()) {
            return preferences.getWakeUpIcon();
        } else if (hour < preferences.getLaunchTime()) {
            return preferences.getD2();
        } else if (hour == preferences.getLaunchTime()) {
            return preferences.getLaunchIcon();
        } else if (hour < preferences.getDinnerTime()) {
            return preferences.getD3();
        } else if (hour == preferences.getDinnerTime()) {
            return preferences.getDinnerIcon();
        } else if (hour < preferences.getBedTime()) {
            return preferences.getEveningIcon();
        } else if (hour >= preferences.getBedTime()) {
            return preferences.getSleepIcon();
        }
        return null;
    }

    private void updateHourLabel(String hourAsString) {
        TextView tv = (TextView) getChildAt(2);
        tv.setText(hourAsString);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }
}
