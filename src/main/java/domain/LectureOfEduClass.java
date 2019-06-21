package domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.entity.PlanningPin;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * Created by xcy on 2019/5/20.
 */
@PlanningEntity(difficultyWeightFactoryClass = LectureDifficultyWeightFactory.class)
public class LectureOfEduClass implements Comparable<LectureOfEduClass>, Serializable {

    private static final long serialVersionUID = 5281845023098929368L;

    @PlanningId
    private Long id;
    private int lectureIndex;
    private boolean pinned;
    private EduClass eduClass;
    private Subject subject;

    // Planning variables: changes during planning, between score calculations.
    private Period period;
    private Room room;
    private Teacher teacher;

    public int getLectureIndex() {
        return lectureIndex;
    }

    public void setLectureIndex(int lectureIndex) {
        this.lectureIndex = lectureIndex;
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

    @PlanningVariable(valueRangeProviderRefs = {"teacherRange"})
    public Teacher getTeacher() {
        return teacher;
    }

    @ValueRangeProvider(id = "teacherRange")
    public List<Teacher> getTeacherRange(){
        return subject.getPossibleTeacher();
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
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

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public int compareTo(LectureOfEduClass o) {
        return this.eduClass.compareTo(o.getEduClass());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureOfEduClass that = (LectureOfEduClass) o;
        return lectureIndex == that.lectureIndex &&
                Objects.equals(id, that.id) &&
                Objects.equals(eduClass, that.eduClass) &&
                Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lectureIndex, eduClass, subject);
    }

    @Override
    public String toString() {
        return "<" + id + "," + eduClass + "," + subject + ", " + teacher +"," + lectureIndex +">";
    }

}
