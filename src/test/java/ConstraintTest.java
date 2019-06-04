import domain.Course;
import domain.Day;
import domain.EduClass;
import domain.Lecture;
import domain.Period;
import domain.Room;
import domain.Teacher;
import domain.TimeTablingProblem;
import domain.Timeslot;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.BeforeClass;
import org.junit.Test;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.test.impl.score.buildin.hardsoft.HardSoftScoreVerifier;
import util.Dataset;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcy on 2019/5/27.
 */

public class ConstraintTest {
    private static TimeTablingProblem problem;
    private HardSoftScoreVerifier<TimeTablingProblem> scoreVerifier = new
            HardSoftScoreVerifier<>(
            SolverFactory.createFromXmlResource(
                    "config/solverConfig.xml"));

    @BeforeClass
    public static void beforeClass() throws IOException, ClassNotFoundException {
        //读入初始数据
        problem = new TimeTablingProblem();
        Dataset.createDataset(problem,"时间地点2.csv","分班数据2.csv","教学资源2.csv");
        //读入排课结果
        InputStreamReader in = new InputStreamReader(new FileInputStream("result1.csv"), "gbk");
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        List<Lecture> lectures = new ArrayList<>();
        for (CSVRecord record : records) {
            Day d = new Day();
            d.setDayIndex(Integer.parseInt(record.get("day")));
            Timeslot t = new Timeslot();
            t.setTimeslotIndex(Integer.parseInt(record.get("timeslot")));
            Period period = new Period();
            period.setDay(d);
            period.setTimeslot(t);
            Room room = new Room();
            room.setName(record.get("room"));
            EduClass eduClass = new EduClass();
            eduClass.setName(record.get("eduClass"));
            eduClass.setType(Integer.parseInt(record.get("eduClassType")));

            Course course = new Course();
            Teacher teacher = new Teacher();
            teacher.setName(record.get("teacher"));
            course.setName(record.get("name"));
            course.setTeacher(teacher);
            course.setLectureSize(Integer.parseInt(record.get("lectureSize")));
            course.setClassNo(Integer.parseInt(record.get("classNo")));
            course.setType(Integer.parseInt(record.get("type")));

            Lecture lecture = new Lecture();
            lecture.setId(Long.parseLong(record.get("id")));
            lecture.setPeriod(period);
            lecture.setRoom(room);
            lecture.setLectureIndexInCourse(Integer.parseInt(record.get("lectureIndexInCourse")));
            lecture.setCourse(course);
            lecture.setEduClass(eduClass);
            lectures.add(lecture);
        }
        problem.setLectureList(lectures);
    }

    @Test
    public void conflictingLecturesDifferentCourseInSamePeriod() {
        scoreVerifier.assertHardWeight("conflictingLecturesDifferentCourseInSamePeriod", 0, problem);
    }

    @Test
    public void conflictingEduClassInSamePeriod() {
        scoreVerifier.assertHardWeight("conflictingEduClassInSamePeriod", 0, problem);
    }

    @Test
    public void roomOccupancy() {
        scoreVerifier.assertHardWeight("roomOccupancy", 0, problem);
    }

    @Test
    public void administrativeClass() {
        scoreVerifier.assertHardWeight("administrativeClass", 0, problem);
    }

    @Test
    public void teachingClass() {
        scoreVerifier.assertHardWeight("teachingClass", 0, problem);
    }

    @Test
    public void sameRoom() {
        scoreVerifier.assertHardWeight("sameRoom", 0, problem);
    }

    @Test
    public void roomStability() {
        scoreVerifier.assertHardWeight("roomStability", 0, problem);
    }

/*    @Test
    public void conflictingLecturesSameCourseInSamePeriod(){
        scoreVerifier.assertHardWeight("conflictingLecturesSameCourseInSamePeriod", 0, problem);
    }*/
}
