package it.andreagaspardo.clock.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;

import it.andreagaspardo.clock.R;

public class DayRowLayout extends GridLayout {
    private int from;
    private int to;

    public DayRowLayout(Context context) {
        super(context);
        initComponent();
    }

    public DayRowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public DayRowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    private void initComponent() {
        setPadding(4, 4, 4, 4);
    }

    public void setInterval(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public void setCurrentHour(int hour) {
        if (hour >= from && hour <= to) {
            setBackgroundColor(getResources().getColor(R.color.current_section,
                    getContext().getTheme()));
        } else {
            setBackgroundColor(getResources().getColor(R.color.hours_bg,
                    getContext().getTheme()));
        }
    }
}
