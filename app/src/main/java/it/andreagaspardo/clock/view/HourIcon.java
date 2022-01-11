package it.andreagaspardo.clock.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.andreagaspardo.clock.R;

public class HourIcon extends androidx.appcompat.widget.AppCompatImageView implements DayViewComponent {
    public HourIcon(@NonNull Context context) {
        super(context);
    }

    public HourIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void updateStatus(boolean done, boolean current) {
        float f = 1;
        if (!done) {
            f = (float) 0.2;
        }
        setAlpha(f);
        int bg;
        if (current) {
            bg = getResources().getColor(R.color.primary, getContext().getTheme());
        } else {
            bg = getResources().getColor(R.color.bg, getContext().getTheme());
        }

        setBackgroundColor(bg);
    }
}
