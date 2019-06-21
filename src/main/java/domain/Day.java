package domain;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by xcy on 2019/5/20.
 */
public class Day implements Serializable {

    private static final long serialVersionUID = -3738174756703337500L;

    @PlanningId
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
