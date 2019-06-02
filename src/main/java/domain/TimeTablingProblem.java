package domain;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                        .filter(rightStudents::contains)
                        .collect(Collectors.counting());
                if (conflictCount > 0)
                    eduClassConflictList.add(new EduClassConflict(leftEduClass, rightEduClass, (int) conflictCount));
            }
        }
        //与自身构成冲突
        for (EduClass eduClass : eduClassList) {
             eduClassConflictList.add(new EduClassConflict(eduClass, eduClass, eduClass.getStudents().size()));
        }

        if(!Files.exists(Paths.get("classConflict.csv"))){
            try (
                    BufferedWriter writer = Files.newBufferedWriter(Paths.get("classConflict.csv"), Charset.forName("gbk"));
                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("leftEduClass","rightEduClass"))
            ) {
                eduClassConflictList.forEach(e -> {
                    try {
                        csvPrinter.printRecord(e.getLeftEduClass(),e.getRightEduClass());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                });
                csvPrinter.flush();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return eduClassConflictList;
    }

    @ProblemFactCollectionProperty
    private List<CourseConflict> calculateCourseConflict() {
        List<CourseConflict> courseConflictList = new ArrayList<>();

        int size = courseList.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
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
