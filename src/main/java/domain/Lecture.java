package domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.entity.PlanningPin;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.List;

/**
 * Created by xcy on 2019/5/20.
 */
@PlanningEntity
public class Lecture implements Comparable<Lecture>{

    private Long id;
    private Course course;
    private int lectureIndexInCourse;
    private boolean pinned;

    // Planning variables: changes during planning, between score calculations.
    private Period period;
    private Room room;
    private EduClass eduClass;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getLectureIndexInCourse() {
        return lectureIndexInCourse;
    }

    public void setLectureIndexInCourse(int lectureIndexInCourse) {
        this.lectureIndexInCourse = lectureIndexInCourse;
    }

    @PlanningPin
    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    @PlanningVariable(valueRangeProviderRefs = {"periodRange"})
    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @PlanningVariable(valueRangeProviderRefs = {"roomRange"})
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    public Teacher getTeacher() {
        return course.getTeacher();
    }

    public Day getDay() {
        if (period == null) {
            return null;
        }
        return period.getDay();
    }

    public int getTimeslotIndex() {
        if (period == null) {
            return Integer.MIN_VALUE;
        }
        return period.getTimeslot().getTimeslotIndex();
    }

    public String getLabel() {
        return course.getName() + "-" + lectureIndexInCourse;
    }

    @Override
    public String toString() {
        return course + "-" + lectureIndexInCourse;
    }

    @PlanningVariable(valueRangeProviderRefs = {"eduClassRange"})
    public EduClass getEduClass() {
        return eduClass;
    }

    public void setEduClass(EduClass eduClass) {
        this.eduClass = eduClass;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<EduClass> getPossibleEduClassList(){
        return getCourse().getPossibleEduClassList();
    }


    @Override
    public int compareTo(Lecture o) {
        return this.course.compareTo(o.course);
    }
}
