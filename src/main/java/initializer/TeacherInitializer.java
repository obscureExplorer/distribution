package initializer;

import domain.EduClass;
import domain.LectureOfEduClass;
import domain.Subject;
import domain.Teacher;
import domain.TimeTablingProblem;
import org.optaplanner.core.impl.phase.custom.AbstractCustomPhaseCommand;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Created by xcy on 2019/6/20.
 */
public class TeacherInitializer  extends AbstractCustomPhaseCommand<TimeTablingProblem> {
    @Override
    public void changeWorkingSolution(ScoreDirector<TimeTablingProblem> scoreDirector) {
        TimeTablingProblem problem = scoreDirector.getWorkingSolution();
        List<LectureOfEduClass> lectures = problem.getLectureList();
        Map<Integer, Map<Subject, List<Teacher>>> subjectMap = problem.getSubjectMap();
        Map<Subject, Map<EduClass, List<LectureOfEduClass>>> lectureMap = lectures.stream().collect(Collectors.groupingBy(LectureOfEduClass::getSubject, Collectors.groupingBy(LectureOfEduClass::getEduClass)));

        for (Subject subject : lectureMap.keySet()) {
            List<Teacher> teachers = subjectMap.get(subject.getType()).get(subject);
            Queue<Teacher> teacherQueue = new LinkedList<>();
            for (Teacher teacher : teachers) {
                for (int i = 0; i < teacher.getMaxClassNum(); i++) {
                    teacherQueue.add(teacher);
                }
            }
            for (EduClass eduClass : lectureMap.get(subject).keySet()) {
                Teacher currentTeacher = teacherQueue.remove();
                for (LectureOfEduClass lecture : lectureMap.get(subject).get(eduClass)) {
                    scoreDirector.beforeVariableChanged(lecture,"teacher");
                    lecture.setTeacher(currentTeacher);
                    scoreDirector.afterVariableChanged(lecture,"teacher");
                    scoreDirector.triggerVariableListeners();
                }
            }
        }
    }
}
