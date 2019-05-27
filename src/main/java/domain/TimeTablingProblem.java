package domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xcy on 2019/5/15.
 */
@PlanningSolution
public class TimeTablingProblem  implements Serializable {

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
    private List<EduClass> eduClassList;

    @ProblemFactCollectionProperty
    private List<Course> courseList;

    @PlanningEntityCollectionProperty
    private List<Lecture> lectureList;

    @PlanningScore
    private HardSoftScore score;

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

    public List<Lecture> getLectureList() {
        return lectureList;
    }

    public void setLectureList(List<Lecture> lectureList) {
        this.lectureList = lectureList;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public List<EduClass> getEduClassList() {
        return eduClassList;
    }

    public void setEduClassList(List<EduClass> eduClassList) {
        this.eduClassList = eduClassList;
    }

    @ProblemFactCollectionProperty
    private List<EduClassConflict> calculateClassConflict(){
        List<EduClassConflict> eduClassConflictList = new ArrayList<>();
        int size = eduClassConflictList.size();
        for(int i = 0 ; i < size - 1; i++){
            for(int j = i + 1; j < size; j++){
                EduClass leftEduClass = eduClassList.get(i);
                EduClass rightEduClass = eduClassList.get(j);

                List<Student> leftStudents = leftEduClass.getStudents();
                List<Student> rightStudents = rightEduClass.getStudents();
                long conflictCount = leftStudents.stream()
                        .filter(rightStudents::contains)
                        .collect(Collectors.counting());
                if(conflictCount > 0)
                    eduClassConflictList.add(new EduClassConflict(leftEduClass,rightEduClass, (int) conflictCount));
            }
        }

        return eduClassConflictList;
    }

    @ProblemFactCollectionProperty
    private List<CourseConflict> calculateCourseConflict(){
        List<CourseConflict> courseConflictList = new ArrayList<>();

        int size = courseList.size();
        for (int i = 0; i < size - 1; i++) {
            for(int j = i + 1; j < size; j++){
                Course leftCourse = courseList.get(i);
                Course rightCourse = courseList.get(j);
                int conflictCount = 0;
                if (leftCourse.getTeacher().equals(rightCourse.getTeacher())) {
                    conflictCount++;
                }
                if (conflictCount > 0) {
                    courseConflictList.add(new CourseConflict(leftCourse, rightCourse, conflictCount));
                }
            }
        }

        return courseConflictList;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }
}
