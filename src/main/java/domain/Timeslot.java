package domain;

import java.io.Serializable;

/**
 * Created by xcy on 2019/5/20.
 */
public class Timeslot implements Serializable {
    private static final String[] TIMES = {"08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"};

    private int timeslotIndex;

    public int getTimeslotIndex() {
        return timeslotIndex;
    }

    public void setTimeslotIndex(int timeslotIndex) {
        this.timeslotIndex = timeslotIndex;
    }

    public String getLabel() {
        String time = TIMES[timeslotIndex % TIMES.length];
        if (timeslotIndex > TIMES.length) {
            return "Timeslot " + timeslotIndex;
        }
        return time;
    }

    @Override
    public String toString() {
        return Integer.toString(timeslotIndex);
    }

}
