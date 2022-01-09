package it.andreagaspardo.clock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.graphics.ColorUtils;

import it.andreagaspardo.clock.R;
import it.andreagaspardo.clock.model.Helper;
import it.andreagaspardo.clock.model.HourModel;

import java.util.Locale;

/**
 * Old hour view.
 *
 * @deprecated
 */
public class EasyHourView extends View {
    private boolean isInit;
    private Paint paint;
    private int fontSizeSmall;
    private int height;
    private int width;
    private HourModel hourModel;

    public EasyHourView(Context context) {
        super(context);
    }

    public EasyHourView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyHourView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isInit) {
            initClock();
        }
        canvas.drawColor(getBgColor());
        hourModel.reset();
        drawHourBoxes(canvas);
        postInvalidateDelayed(500);
        invalidate();
    }

    /**
     * Init clock.
     */
    private void initClock() {
        hourModel = new HourModel(Helper.getCurrentLocale(getContext()));
        height = getHeight();
        width = getWidth();
        fontSizeSmall = height / 10;
        paint = new Paint();
        isInit = true;
    }

    /**
     * Draw hour boxes.
     *
     * @param canvas Canvas
     */
    private void drawHourBoxes(Canvas canvas) {
        float top = (float) (height * 0.05);
        float rectHeight = (float) (height * 0.90);
        float left = (float) (width * 0.05);
        float rectWidth = (float) (width * 0.9);
        int rows = 2;
        int cols = 12;

        int currentHour = hourModel.getHour();

        float rWidth = rectWidth / cols;
        float rHeight = rectHeight / rows;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                drawHourBox(canvas, row * cols + col, col * rWidth, row * rHeight, rHeight, rWidth, top, left, currentHour, row);
            }
        }
    }

    private void drawHourBox(Canvas canvas, int hour, float x, float y, float h, float w, float top, float left, int currentHour, int row) {
        float padding = w * (float) 0.06;
        float l = left + x + padding;
        float t = top + y + padding;
        float r = left + x + w - padding;
        float b = top + y + h - padding;

        if (row == 0) {
            t += h / 2.5;
        } else {
            b -= h / 2.5;
        }

        paint.setStyle(Paint.Style.FILL);
        if (hour == currentHour) {
            paint.setColor(getCurrentHourColor());
            canvas.drawRect(l, t, r, b, paint);
        } else if (hour < currentHour) {
            paint.setColor(getFilledHourColor());
            canvas.drawRect(l, t, r, b, paint);
        }

        paint.setColor(getRectStrokeColor());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(l, t, r, b, paint);

        paint.setColor(getTextColor());
        paint.setStyle(Paint.Style.FILL);

        paint.setTextSize(fontSizeSmall);
        Rect hourRect = new Rect();
        String hourText = String.valueOf(hour);
        paint.getTextBounds(hourText, 0, hourText.length(), hourRect);
        float b1 = row == 0 ? (float) (b - (h / 1.6)) : (float) (b + (h / 2.8));
        canvas.drawText(hourText, l + (r - l - hourRect.width()) / 2, b1, paint);
    }

    private int getCurrentHourColor() {
        return getResources().getColor(R.color.bold, getContext().getTheme());
    }

    private int getRectStrokeColor() {
        return ColorUtils.blendARGB(getTextColor(), getFilledHourColor(), 0.7f);
    }

    private int getFilledHourColor() {
        return getResources().getColor(R.color.primary, getContext().getTheme());
    }

    private int getTextColor() {
        return getResources().getColor(R.color.text_bounds_color, getContext().getTheme());
    }

    private int getBgColor() {
        return getResources().getColor(R.color.bg, getContext().getTheme());
    }
}