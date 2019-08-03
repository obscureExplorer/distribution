package domain;

import com.google.common.base.Objects;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.io.Serializable;

/**
 * Created by xcy on 2019/5/20.
 */
public class TimeSlot implements Serializable {
    private static final long serialVersionUID = -7629783018536847059L;

    @PlanningId
    private Long id;

    private int timeSlotIndex;

    public int getTimeSlotIndex() {
        return timeSlotIndex;
    }

    public void setTimeSlotIndex(int timeSlotIndex) {
        this.timeSlotIndex = timeSlotIndex;
    }

    @Override
    public String toString() {
        return Integer.toString(timeSlotIndex);
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
        TimeSlot timeslot = (TimeSlot) o;
        return timeSlotIndex == timeslot.timeSlotIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(timeSlotIndex);
    }
}
