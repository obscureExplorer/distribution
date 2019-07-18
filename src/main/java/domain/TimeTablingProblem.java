package domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by xcy on 2019/5/15.
 */
@PlanningSolution
public class TimeTablingProblem implements Serializable {

    private static final long serialVersionUID = 2869218948801133466L;

    @ProblemFactCollectionProperty
    private List<Day> dayList;

    @ProblemFactCollectionProperty
    private List<Timeslot> timeslotList;

    @ValueRangeProvider(id = "periodRange")
    @ProblemFactCollectionProperty
    private List<Period> periodList;

    @ValueRangeProvider(id = "roomRange")
    @ProblemFactCollectionProperty
    private List<Room> roomList;

    @ProblemFactCollectionProperty
    private List<Teacher> teacherList;

    @ProblemFactCollectionProperty
    private List<EduClass> eduClassList;

    @ProblemFactCollectionProperty
    private List<Subject> subjectList;

    @ProblemFactCollectionProperty
    private List<PeriodPenalty> periodPenaltyList;

    @PlanningEntityCollectionProperty
    private List<LectureOfEduClass> lectureList;

    @ProblemFactCollectionProperty
    private List<TeacherAssignment> teacherAssignmentList;

/*    @ProblemFactCollectionProperty
    private List<Group> groupList;

    @ProblemFactCollectionProperty
    private List<GroupEduClass> groupEduClassList;*/

    private Map<Subject, Collection<Teacher>> subjectTeacherMap;

    @PlanningScore
    private HardMediumSoftScore score;

    public List<Day> getDayList() {
        return dayList;
    }

    public void setDayList(List<Day> dayList) {
        this.dayList = dayList;
    }

    public List<Timeslot> getTimeslotList() {
        return timeslotList;
    }

    public void setTimeslotList(List<Timeslot> timeslotList) {
        this.timeslotList = timeslotList;
    }

    public List<Period> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<Period> periodList) {
        this.periodList = periodList;
    }

    public List<LectureOfEduClass> getLectureList() {
        return lectureList;
    }

    public void setLectureList(List<LectureOfEduClass> lectureList) {
        this.lectureList = lectureList;
    }

    public HardMediumSoftScore getScore() {
        return score;
    }

    public void setScore(HardMediumSoftScore score) {
        this.score = score;
    }

    public List<EduClass> getEduClassList() {
        return eduClassList;
    }

    public void setEduClassList(List<EduClass> eduClassList) {
        this.eduClassList = eduClassList;
    }

    @ProblemFactCollectionProperty
    private List<EduClassConflict> calculateClassConflict() {
        List<EduClassConflict> eduClassConflictList = new ArrayList<>();
        int size = eduClassList.size();
        //班级与班级之间的冲突
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                EduClass leftEduClass = eduClassList.get(i);
                EduClass rightEduClass = eduClassList.get(j);

                List<Student> leftStudents = leftEduClass.getStudents();
                List<Student> rightStudents = rightEduClass.getStudents();
                long conflictCount = leftStudents.stream()
                        .filter(rightStudents::contains).count();
                if (conflictCount > 0)
                   eduClassConflictList.add(new EduClassConflict(leftEduClass, rightEduClass, (int) conflictCount));
                    //eduClassConflictList.add(new EduClassConflict(leftEduClass, rightEduClass, 1));
            }
        }
        return eduClassConflictList;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    public Map<Subject, Collection<Teacher>> getSubjectTeacherMap() {
        return subjectTeacherMap;
    }

    public void setSubjectTeacherMap(Map<Subject, Collection<Teacher>> subjectTeacherMap) {
        this.subjectTeacherMap = subjectTeacherMap;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public List<PeriodPenalty> getPeriodPenaltyList() {
        return periodPenaltyList;
    }

    public void setPeriodPenaltyList(List<PeriodPenalty> periodPenaltyList) {
        this.periodPenaltyList = periodPenaltyList;
    }

    public List<TeacherAssignment> getTeacherAssignmentList() {
        return teacherAssignmentList;
    }

    public void setTeacherAssignmentList(List<TeacherAssignment> teacherAssignmentList) {
        this.teacherAssignmentList = teacherAssignmentList;
    }

/*    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List<GroupEduClass> getGroupEduClassList() {
        return groupEduClassList;
    }

    public void setGroupEduClassList(List<GroupEduClass> groupEduClassList) {
        this.groupEduClassList = groupEduClassList;
    }*/
}
