package it.andreagaspardo.clock.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import it.andreagaspardo.clock.model.Helper;
import it.andreagaspardo.clock.model.HourModel;
import it.andreagaspardo.clock.model.Preferences;

public class EasyHourTextView extends LinearLayout {
    private final Timer timer = new Timer(true);
    private HourModel hourModel;

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
        initTimer();
    }

    private void initTimer() {
        hourModel = new HourModel(Helper.getCurrentLocale(getContext()));
        updateHour();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateHour();
            }
        }, 1000);
        Preferences preferences = new Preferences(getContext());
        if (preferences.uppercase()) {
            TextView tv = (TextView) getChildAt(0);
            tv.setText(tv.getText().toString().toUpperCase());
        }
    }

    public void updateHour() {
        hourModel.reset();
        TextView tv = (TextView) getChildAt(1);
        tv.setText(hourModel.getHourAsString());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }
}
