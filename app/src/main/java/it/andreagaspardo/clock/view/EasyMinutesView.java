package it.andreagaspardo.clock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.core.graphics.ColorUtils;

import it.andreagaspardo.clock.R;
import it.andreagaspardo.clock.model.HourModel;
import it.andreagaspardo.clock.model.Preferences;

import java.util.Locale;

/**
 * Minutes view.
 */
public class EasyMinutesView extends View {
    private int height, width = 0;
    private int padding = 0;
    private int fontSize = 0;
    private float radius = 0;
    private Paint paint;
    private boolean isInit;
    private final Rect rect = new Rect();
    private int fontSizeBig;
    private int fontSizeSmall;
    private HourModel hourModel;
    private Preferences preferences;
    private float handTruncation = 0;

    public EasyMinutesView(Context context) {
        super(context);
    }

    public EasyMinutesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyMinutesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        preferences = new Preferences(getContext());
        if (!preferences.showMinutesComponent()) {
            setVisibility(INVISIBLE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isInit) {
            initClock();
        }
        hourModel.reset();

        drawCircle(canvas);
        drawHands(canvas);
        if (preferences.showQuadrants()) {
            drawQuadrants(canvas);
        }
        if (preferences.showMinutes()) {
            drawNumeral(canvas);
        }
        if (preferences.showMinutesNumber()) {
            drawHourText(canvas);
        }

        postInvalidateDelayed(30 * 1000);
        invalidate();
    }

    private void drawHand(Canvas canvas, float loc) {
        double angle = 180 * loc / 30;

        paint.setStyle(Paint.Style.FILL);
        float r = (float) (radius * .9);
        RectF oval = new RectF((float) width / 2 - r,
                (float) height / 2 - r,
                (float) width / 2 + r,
                (float) height / 2 + r);
        paint.setColor(ColorUtils.blendARGB(getBgColor(), Color.BLACK, 0.1f));
        canvas.drawOval(oval, paint);

        paint.setColor(getPrimaryColor());
        canvas.drawArc(oval,
                (float) -90,
                (float) angle, true, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(ColorUtils.blendARGB(getPrimaryColor(), Color.BLACK, 0.2f));
        canvas.drawArc(oval,
                (float) -90,
                (float) angle, true, paint);
        drawHandLine(canvas, loc);
    }

    private void drawHandLine(Canvas canvas, double loc) {
        double angle = Math.PI * loc / 30 - Math.PI / 2;
        float handRadius = radius - handTruncation;
        paint.setStrokeWidth(10);
        paint.setColor(getHandColor());
        canvas.drawLine((int) (width / 2), (int) (height / 2),
                (float) (width / 2 + Math.cos(angle) * handRadius),
                (float) (height / 2 + Math.sin(angle) * handRadius),
                paint);
    }

    private void drawHourText(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ColorUtils.blendARGB(getBgColor(), Color.BLACK, 0.7f));
        paint.setTextSize(fontSizeBig);

        Rect hourRect = new Rect();
        String hourText = hourModel.getMinutesAsString();
        paint.getTextBounds(hourText, 0, hourText.length(), hourRect);
        canvas.drawText(hourText,
                (int) (width / 2 - hourRect.width() / 2),
                (int) (height / 2.3 + hourRect.height() / 2), paint);

        paint.setTextSize(fontSizeSmall);
        String minutesText = getResources().getText(R.string.minutes).toString();
        if (preferences.uppercase()) {
            minutesText = minutesText.toUpperCase();
        }
        paint.getTextBounds(minutesText, 0, minutesText.length(), hourRect);
        canvas.drawText(minutesText,
                (int) (width / 2 - hourRect.width() / 2),
                (int) (height / 1.8 + hourRect.height()), paint);
    }

    private void drawHands(Canvas canvas) {
        drawHand(canvas, hourModel.getMinutes());
    }

    private void drawQuadrants(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(ColorUtils.blendARGB(getBgColor(), Color.BLACK, 0.4f));
        float r = (float) (radius * .85);
        float cX = (float) width / 2;
        float cY = (float) height / 2;
        canvas.drawLine(cX - r, cY, cX + r, cY, paint);
        canvas.drawLine(cX, cY - r, cX, cY + r, paint);
    }

    private void drawNumeral(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(fontSize);

        for (int number = 0; number < 60; number++) {
            if (number % 5 == 0) {
                paint.setColor(getPrimaryColor());
            } else {
                paint.setColor(getTextColor());
            }
            String tmp = String.valueOf(number);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = Math.PI / 30 * (number - 15);
            int x = (int) (width / 2 + Math.cos(angle) * radius - rect.width() / 2);
            int y = (int) (height / 2 + Math.sin(angle) * radius + rect.height() / 2);
            canvas.drawText(tmp, x, y, paint);
        }
    }

    private void drawCircle(Canvas canvas) {
        paint.reset();
        paint.setColor(ColorUtils.blendARGB(getBgColor(), Color.BLACK, 0.05f));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle((int) (width / 2), (int) (height / 2), radius + padding - 10, paint);
    }

    private void initClock() {
        hourModel = new HourModel(Locale.ITALY);

        height = getHeight();
        width = getWidth();
        int numeralSpacing = 0;
        padding = numeralSpacing + 50;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13,
                getResources().getDisplayMetrics());
        float min = Math.min(height, width);
        radius = min / 2 - padding;
        paint = new Paint();

        fontSizeBig = (int) (height / 4);
        fontSizeSmall = (int) (fontSizeBig / 2.5);

        radius = min / 2 - padding;
        handTruncation = min / 22;

        isInit = true;
    }

    private int getBgColor() {
        return getResources().getColor(R.color.bg, getContext().getTheme());
    }

    private int getTextColor() {
        return getResources().getColor(R.color.text_bounds_color, getContext().getTheme());
    }

    private int getPrimaryColor() {
        return getResources().getColor(R.color.primary, getContext().getTheme());
    }

    private int getHandColor() {
        return getResources().getColor(R.color.hand, getContext().getTheme());
    }
}