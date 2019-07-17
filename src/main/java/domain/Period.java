package domain;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by xcy on 2019/5/20.
 */
public class Period implements Serializable {
    private static final long serialVersionUID = 2761408999678903490L;

    @PlanningId
    private Long id;
    private Day day;
    private Timeslot timeslot;

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public String toString() {
        return day + "_" + timeslot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(day, period.day) &&
                Objects.equals(timeslot, period.timeslot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, timeslot);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
