package domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by xcy on 2019/5/20.
 */
public class Day implements Serializable {

    private static final String[] WEEKDAYS = {"Mo", "Tu", "We", "Th", "Fr", "Sat", "Sun"};

    private int dayIndex;

    private List<Period> periodList;

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    public List<Period> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<Period> periodList) {
        this.periodList = periodList;
    }

    public String getLabel() {
        String weekday = WEEKDAYS[dayIndex % WEEKDAYS.length];
        if (dayIndex > WEEKDAYS.length) {
            return "Day " + dayIndex;
        }
        return weekday;
    }

    @Override
    public String toString() {
        return Integer.toString(dayIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day = (Day) o;
        return dayIndex == day.dayIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayIndex);
    }
}
