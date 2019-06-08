package domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * Created by xcy on 2019/6/7 0007.
 */
@PlanningEntity
public class RealEduClass {
    private int id;
    private int index;
    private EduClass eduClass;

    @PlanningVariable(valueRangeProviderRefs = {"periodRange"})
    private Period period;

    @PlanningVariable(valueRangeProviderRefs = {"roomRange"})
    private Room room;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public EduClass getEduClass() {
        return eduClass;
    }

    public void setEduClass(EduClass eduClass) {
        this.eduClass = eduClass;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "<" + eduClass + "," + index + ">";
    }
}
