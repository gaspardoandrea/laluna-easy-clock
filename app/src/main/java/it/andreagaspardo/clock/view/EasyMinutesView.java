package it.andreagaspardo.clock.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
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
    private float centerX, centerY;
    private final float pi = (float)Math.PI;
    private float fontSize = 0;
    private Paint paint;
    private boolean isInit;
    private final Rect rect = new Rect();
    private float fontSizeBig;
    private float fontSizeSmall;
    private HourModel hourModel;
    private Preferences preferences;

    private float radius;
    private float handRadius;
    private float numeralRadius;
    private float quadrantRadius;

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

    private void initClock() {
        hourModel = new HourModel(Locale.ITALY);

        float height = getHeight();
        float width = getWidth();
        centerX = width / 2;
        centerY = height / 2;

        float min = Math.min(height, width);
        fontSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, min / 80f,
                getResources().getDisplayMetrics());
        paint = new Paint();

        fontSizeBig = min / 4f;
        fontSizeSmall = (int) (fontSizeBig / 2.5);

        radius = min / 2 - 10f;
        handRadius = radius * .85f;
        numeralRadius = radius * 0.93f;
        quadrantRadius = radius * .80f;

        isInit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isInit) {
            initClock();
        }
        hourModel.reset();

        paint.reset();
        paint.setAntiAlias(true);

        drawHands(canvas);
        if (preferences.showQuadrants()) {
            drawQuadrants(canvas);
        }
        if (preferences.showMinutes()) {
            drawNumeralBg(canvas);
            drawNumeral(canvas);
        }
        if (preferences.showMinutesNumber()) {
            drawHourText(canvas);
        }
        drawHourCover(canvas);

        postInvalidateDelayed(30 * 1000);
        invalidate();
    }

    private void drawHand(Canvas canvas, float loc) {
        float angle = 180 * loc / 30;

        paint.setStyle(Paint.Style.FILL);
        RectF oval = new RectF(centerX - handRadius,
                centerY - handRadius,
                centerX + handRadius,
                centerY + handRadius);
        paint.setColor(ColorUtils.blendARGB(getBgColor(), Color.BLACK, 0.1f));
        canvas.drawOval(oval, paint);

        paint.setColor(getPrimaryColor());
        canvas.drawArc(oval,
                -90,
                angle, true, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(ColorUtils.blendARGB(getPrimaryColor(), Color.BLACK, 0.2f));
        canvas.drawArc(oval,
                -90,
                angle, true, paint);
        drawHandLine(canvas, loc);
    }

    private void drawHandLine(Canvas canvas, float loc) {
        float angle = pi * loc / 30f - pi / 2f;
        paint.setStrokeWidth(10);
        paint.setColor(getHandColor());
        canvas.drawLine(
                centerX,
                centerY,
                centerX + (float)Math.cos(angle) * handRadius,
                centerY + (float)Math.sin(angle) * handRadius,
                paint);
    }

    private void drawHourText(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ColorUtils.blendARGB(getBgColor(), Color.BLACK, 0.7f));
        paint.setTextSize(fontSizeBig);

        Rect hourRect = new Rect();
        String minutes = hourModel.getMinutesAsString();
        paint.getTextBounds(minutes, 0, minutes.length(), hourRect);
        canvas.drawText(minutes,
                centerX - hourRect.width() / 2f,
                centerY - hourRect.height() / 8f, paint);

        paint.setTextSize(fontSizeSmall);
        String minutesText = getResources().getText(R.string.minutes).toString();
        if (preferences.uppercase()) {
            minutesText = minutesText.toUpperCase();
        }
        paint.getTextBounds(minutesText, 0, minutesText.length(), hourRect);
        canvas.drawText(minutesText,
                (int) (centerX - hourRect.width() / 2f),
                (int) (centerY + hourRect.height() * 1.5f), paint);
    }

    private void drawHands(Canvas canvas) {
        drawHand(canvas, hourModel.getMinutes());
    }

    private void drawQuadrants(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(getQuadrantColor());
        canvas.drawLine(centerX - quadrantRadius, centerY, centerX + quadrantRadius, centerY, paint);
        canvas.drawLine(centerX, centerY - quadrantRadius, centerX, centerY + quadrantRadius, paint);
    }

    private void drawNumeralBg(Canvas canvas) {
        for (int number = 0; number < 60; number++) {
            float angle = pi / 30 * (number - 15);
            float angleFrom = angle - pi / 60;
            float angleTo = angle + pi / 60;

            final Path p = new Path();
            float x = (int) (centerX + Math.cos(angleFrom) * handRadius);
            float y = (int) (centerY + Math.sin(angleFrom) * handRadius);
            p.moveTo(x, y);
            x = (int) (centerX + Math.cos(angleTo) * handRadius);
            y = (int) (centerY + Math.sin(angleTo) * handRadius);
            p.lineTo(x, y);
            x = (int) (centerX + Math.cos(angleTo) * radius);
            y = (int) (centerY + Math.sin(angleTo) * radius);
            p.lineTo(x, y);
            x = (int) (centerX + Math.cos(angleFrom) * radius);
            y = (int) (centerY + Math.sin(angleFrom) * radius);
            p.lineTo(x, y);
            p.close();

            if (number % 5 == 0) {
                paint.setColor(getPrimaryColor());
            } else {
                paint.setColor(getBgColor());
            }
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(p, paint);
            paint.setColor(getTextBoundsColor());
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(p, paint);
        }
    }

    private void drawNumeral(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(fontSize);

        for (int number = 0; number < 60; number++) {
            if (number % 5 == 0) {
                paint.setColor(getBgColor());
            } else {
                paint.setColor(getTextColor());
            }
            String tmp = String.valueOf(number);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            float angle = pi / 30 * (number - 15);
            int x = (int) (centerX + Math.cos(angle) * numeralRadius - rect.width() / 2);
            int y = (int) (centerY + Math.sin(angle) * numeralRadius + rect.height() / 2);
            canvas.drawText(tmp, x, y, paint);
        }
    }

    private void drawHourCover(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.sfondoquadrante);
        float rectSize = radius * 2;
        Bitmap small = Bitmap.createScaledBitmap(bitmap,
                (int)rectSize, (int)rectSize, true);
        canvas.drawBitmap(small,
                centerX - rectSize / 2,
                centerY - rectSize / 2, paint);
    }

    private int getBgColor() {
        return getResources().getColor(R.color.bg, getContext().getTheme());
    }

    private int getQuadrantColor() {
        return getResources().getColor(R.color.quadrants, getContext().getTheme());
    }

    private int getTextColor() {
        return getResources().getColor(R.color.text_color, getContext().getTheme());
    }

    private int getTextBoundsColor() {
        return getResources().getColor(R.color.text_bounds_color, getContext().getTheme());
    }

    private int getPrimaryColor() {
        return getResources().getColor(R.color.primary, getContext().getTheme());
    }

    private int getHandColor() {
        return getResources().getColor(R.color.hand, getContext().getTheme());
    }
}