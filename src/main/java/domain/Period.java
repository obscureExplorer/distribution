package domain;

import java.io.Serializable;

/**
 * Created by xcy on 2019/5/20.
 */
public class Period implements Serializable {
    private static final long serialVersionUID = 2761408999678903490L;

    private Day day;
    private Timeslot timeslot;
    private int index;

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

    public String getLabel() {
        return day.getLabel() + " " + timeslot.getLabel();
    }

    @Override
    public String toString() {
        return day + "_" + timeslot;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
