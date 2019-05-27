import domain.EduClass;
import domain.Lecture;
import domain.Period;
import domain.Room;
import domain.TimeTablingProblem;
import org.junit.BeforeClass;
import org.junit.Test;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.test.impl.score.buildin.hardsoft.HardSoftScoreVerifier;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
        // read object from file
        FileInputStream fis = new FileInputStream("mybean.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        problem = (TimeTablingProblem) ois.readObject();

        List<Lecture> lectures = problem.getLectureList();
        List<EduClass> eduClasses = problem.getEduClassList();
        List<Period> periods = problem.getPeriodList();
        List<Room> rooms = problem.getRoomList();
        for (int i = 0; i < lectures.size(); i += 2) {
            Lecture p = lectures.get(i);
            Lecture n = lectures.get(i + 1);
            if (i < 12) {
                p.setEduClass(eduClasses.get(i / 6));
                n.setEduClass(eduClasses.get(i / 6));
            } else {
                p.setEduClass(eduClasses.get(i / 2 - 4));
                n.setEduClass(eduClasses.get(i / 2 - 4));
            }
        }
        //语文-张-1
        lectures.get(0).setRoom(rooms.get(0));
        lectures.get(1).setRoom(rooms.get(0));
        lectures.get(0).setPeriod(periods.get(0));
        lectures.get(1).setPeriod(periods.get(1));

        //数学-王-1
        lectures.get(2).setRoom(rooms.get(0));
        lectures.get(3).setRoom(rooms.get(0));
        lectures.get(2).setPeriod(periods.get(2));
        lectures.get(3).setPeriod(periods.get(3));

        //英语-黄-1
        lectures.get(4).setRoom(rooms.get(0));
        lectures.get(5).setRoom(rooms.get(0));
        lectures.get(4).setPeriod(periods.get(4));
        lectures.get(5).setPeriod(periods.get(5));

        //数学-王-2
        lectures.get(6).setRoom(rooms.get(1));
        lectures.get(7).setRoom(rooms.get(1));
        lectures.get(6).setPeriod(periods.get(0));
        lectures.get(7).setPeriod(periods.get(1));

        //英语-黄-2
        lectures.get(8).setRoom(rooms.get(1));
        lectures.get(9).setRoom(rooms.get(1));
        lectures.get(8).setPeriod(periods.get(2));
        lectures.get(9).setPeriod(periods.get(3));

        //语文-张-2
        lectures.get(10).setRoom(rooms.get(1));
        lectures.get(11).setRoom(rooms.get(1));
        lectures.get(10).setPeriod(periods.get(4));
        lectures.get(11).setPeriod(periods.get(5));

        //物理
        lectures.get(12).setRoom(rooms.get(0));
        lectures.get(13).setRoom(rooms.get(0));
        lectures.get(12).setPeriod(periods.get(10));
        lectures.get(13).setPeriod(periods.get(11));

        //化学
        lectures.get(14).setRoom(rooms.get(0));
        lectures.get(15).setRoom(rooms.get(0));
        lectures.get(14).setPeriod(periods.get(6));
        lectures.get(15).setPeriod(periods.get(7));

        //生物
        lectures.get(16).setRoom(rooms.get(1));
        lectures.get(17).setRoom(rooms.get(1));
        lectures.get(16).setPeriod(periods.get(8));
        lectures.get(17).setPeriod(periods.get(9));
        //历史
        lectures.get(18).setRoom(rooms.get(0));
        lectures.get(19).setRoom(rooms.get(0));
        lectures.get(18).setPeriod(periods.get(12));
        lectures.get(19).setPeriod(periods.get(13));
        //政治
        lectures.get(20).setRoom(rooms.get(1));
        lectures.get(21).setRoom(rooms.get(1));
        lectures.get(20).setPeriod(periods.get(6));
        lectures.get(21).setPeriod(periods.get(7));
        //地理
        lectures.get(22).setRoom(rooms.get(0));
        lectures.get(23).setRoom(rooms.get(0));
        lectures.get(22).setPeriod(periods.get(8));
        lectures.get(23).setPeriod(periods.get(9));

        ois.close();

    }

    @Test
    public void conflictingLecturesDifferentCourseInSamePeriod() {
        scoreVerifier.assertHardWeight("conflictingLecturesDifferentCourseInSamePeriod", 0, problem);
    }

    @Test
    public void conflictingEduClassDifferentCourseInSamePeriod() {
        scoreVerifier.assertHardWeight("conflictingEduClassDifferentCourseInSamePeriod", 0, problem);
    }

    @Test
    public void roomOccupancy() {
        scoreVerifier.assertHardWeight("roomOccupancy", 0, problem);
    }

    @Test
    public void administrativeClass(){
        scoreVerifier.assertHardWeight("administrativeClass",0,problem);
    }

    @Test
    public void teachingClass(){
        scoreVerifier.assertHardWeight("teachingClass",0,problem);
    }

    @Test
    public void sameRoom() {
        scoreVerifier.assertHardWeight("sameRoom", 0, problem);
    }
}
