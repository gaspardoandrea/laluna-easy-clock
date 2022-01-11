package it.andreagaspardo.clock.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.andreagaspardo.clock.R;

public class HourText extends androidx.appcompat.widget.AppCompatTextView implements DayViewComponent {
    public HourText(@NonNull Context context) {
        super(context);
        initComponent();
    }

    public HourText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public HourText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    private void initComponent() {
        setTextColor(getResources().getColor(R.color.text_bounds_color,
                getContext().getTheme()));
        setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setPadding(0, 25, 0, 0);

    }

    public void updateText(@NonNull Integer hour) {
        setText(String.valueOf(hour % 24));
    }

    @Override
    public void updateStatus(boolean done, boolean current) {
        setAlpha(done ? 1 : (float) 0.2);
        int bg;
        if (current) {
            bg = getResources().getColor(R.color.primary, getContext().getTheme());
        } else {
            bg = getResources().getColor(R.color.bg, getContext().getTheme());
        }

        setBackgroundColor(bg);

        setTypeface(null, current ? Typeface.BOLD : Typeface.NORMAL);
    }
}
