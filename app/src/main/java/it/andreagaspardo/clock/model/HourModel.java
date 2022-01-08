package it.andreagaspardo.clock.model;

import java.util.Calendar;
import java.util.Locale;

/**
 * Hour model.
 */
public class HourModel {
    private Calendar calendar;
    private Locale locale;

    public HourModel(Locale locale) {
        this.locale = locale;
    }

    /**
     * Reset hour.
     */
    public void reset() {
        calendar = Calendar.getInstance();
    }

    /**
     * Get hour.
     *
     * @return int
     */
    public int getHour() {
        float hour = calendar.get(Calendar.HOUR_OF_DAY);
        return (int)hour;
    }

    /**
     * Get minutes.
     *
     * @return int
     */
    public int getMinutes() {
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * Get seconds.
     *
     * @return int
     */
    public int getSeconds() {
        return calendar.get(Calendar.SECOND);
    }

    /**
     * Get hour as string.
     *
     * @return string
     */
    public String getHourAsString() {
        return String.format(locale, "%d", getHour());
    }

    /**
     * Get minutes as string.
     *
     * @return string
     */
    public String getMinutesAsString() {
        return String.format(locale, "%02d", getMinutes());
    }

    /**
     * Get seconds as string.
     *
     * @return string
     */
    public String getSecondsAsString() {
        return String.format(locale, "%02d", getSeconds());
    }
}
