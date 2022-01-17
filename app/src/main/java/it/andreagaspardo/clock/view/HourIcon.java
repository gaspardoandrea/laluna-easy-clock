package it.andreagaspardo.clock.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.andreagaspardo.clock.R;

public class HourIcon extends androidx.appcompat.widget.AppCompatImageView implements DayViewComponent {
    public HourIcon(@NonNull Context context) {
        super(context);
        initComponent();
    }

    public HourIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }
    private void initComponent() {
        setPadding(5, 5, 15, 5);
    }

    @Override
    public void updateStatus(boolean done, boolean current) {
    }
}
