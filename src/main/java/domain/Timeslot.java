package domain;

import com.google.common.base.Objects;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.io.Serializable;

/**
 * Created by xcy on 2019/5/20.
 */
public class Timeslot implements Serializable {
    private static final long serialVersionUID = -7629783018536847059L;

    @PlanningId
    private Long id;

    private int timeslotIndex;

    public int getTimeslotIndex() {
        return timeslotIndex;
    }

    public void setTimeslotIndex(int timeslotIndex) {
        this.timeslotIndex = timeslotIndex;
    }

    @Override
    public String toString() {
        return Integer.toString(timeslotIndex);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timeslot timeslot = (Timeslot) o;
        return timeslotIndex == timeslot.timeslotIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(timeslotIndex);
    }
}
