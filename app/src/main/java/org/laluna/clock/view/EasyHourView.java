package org.laluna.clock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import org.laluna.clock.R;
import org.laluna.clock.model.HourModel;

import java.util.Locale;

/**
 * TODO: document your custom view class.
 */
public class EasyHourView extends View {
    private boolean isInit;
    private Paint paint;
    private int fontSizeSmall;
    private int fontSizeBig;
    private int fontSizeNormal;
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
        hourModel.reset();
        drawLiteralHour(canvas);
        drawHourBoxes(canvas);
        postInvalidateDelayed(500);
        invalidate();
    }

    /**
     * Init clock.
     */
    private void initClock() {
        // TODO
        hourModel = new HourModel(Locale.ITALY);
        height = getHeight();
        width = getWidth();
        fontSizeSmall = height / 18;
        fontSizeNormal = fontSizeSmall * 4;
        fontSizeBig = fontSizeSmall * 6;
        paint = new Paint();
        isInit = true;
    }

    /**
     * Draw literal hour.
     *
     * @param canvas Canvas
     */
    private void drawLiteralHour(Canvas canvas) {
        // TODO colors
        paint.setColor(getTextColor());
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        paint.setTextSize(fontSizeNormal);
        String hourText = getResources().getText(R.string.hour).toString();
        Rect hourRect = new Rect();
        paint.getTextBounds(hourText, 0, hourText.length(), hourRect);
        int hourRectWidth = (int) (hourRect.width() * 1.2);

        paint.setTextSize(fontSizeBig);
        String hourAsString = hourModel.getHourAsString();
        Rect hourStringRect = new Rect();
        paint.getTextBounds(hourAsString, 0, hourAsString.length(), hourStringRect);

        int totalWidth = hourRectWidth + hourStringRect.width();
        int center = width / 2;
        int marginLeft = center - totalWidth / 2;
        float y = (float) (height / 2.9);

        paint.setTextSize(fontSizeNormal);
        canvas.drawText(hourText, marginLeft, y, paint);
        paint.setTextSize(fontSizeBig);
        canvas.drawText(hourAsString, marginLeft + hourRectWidth, y, paint);
    }

    /**
     * Draw hour boxes.
     *
     * @param canvas Canvas
     */
    private void drawHourBoxes(Canvas canvas) {
        float top = (float) (height * 0.45);
        float rectHeight = (float) (height * 0.5);
        float left = (float) (width * 0.05);
        float rectWidth = (float) (width * 0.9);
        int rows = 2;
        int cols = 12;

        int currentHour = hourModel.getHour();

        float rWidth = rectWidth / cols;
        float rHeight = rectHeight / rows;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                drawHourBox(canvas, row * cols + col, col * rWidth, row * rHeight, rHeight, rWidth, top, left, currentHour);
            }
        }
    }

    private void drawHourBox(Canvas canvas, int hour, float x, float y, float h, float w, float top, float left, int currentHour) {
        float padding = w * (float) 0.1;
        float l = left + x + padding;
        float t = top + y + padding;
        float r = left + x + w - padding;
        float b = top + y + h - padding;

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
        paint.setStrokeWidth(4);
        canvas.drawRect(l, t, r, b, paint);
    }

    private int getCurrentHourColor() {
        return getResources().getColor(R.color.bold, getContext().getTheme());
    }

    private int getRectStrokeColor() {
        return getResources().getColor(R.color.text_bounds_color, getContext().getTheme());
    }

    private int getFilledHourColor() {
        return getResources().getColor(R.color.primary, getContext().getTheme());
    }

    private int getTextColor() {
        return getResources().getColor(R.color.text_bounds_color, getContext().getTheme());
    }
}