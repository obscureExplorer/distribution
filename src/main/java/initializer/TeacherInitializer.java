package initializer;

import domain.EduClass;
import domain.LectureOfEduClass;
import domain.Subject;
import domain.SubjectTypeEnum;
import domain.Teacher;
import domain.TeacherAssignment;
import domain.TimeTablingProblem;
import org.optaplanner.core.impl.phase.custom.AbstractCustomPhaseCommand;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Collection;
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
        //过滤掉老师不能变动的
        Map<Subject, Map<EduClass, List<LectureOfEduClass>>> lectureMap = lectures.stream().filter(l -> !l.isTeacherUnmovable()).collect(Collectors.groupingBy(LectureOfEduClass::getSubject, Collectors.groupingBy(LectureOfEduClass::getEduClass)));
        //查找科目-老师-开班数对应关系
        Map<Subject, List<TeacherAssignment>> assignmentMap = problem.getTeacherAssignmentList().stream().collect(Collectors.groupingBy(TeacherAssignment::getSubject));

        for (Subject subject : lectureMap.keySet()) {
            Queue<Teacher> teacherQueue = new LinkedList<>();
            List<TeacherAssignment> assignments = assignmentMap.get(subject);
            for (TeacherAssignment assignment : assignments) {
                int maxClassNo = assignment.getMaxClassNo();
                for (int i = 0; i < maxClassNo; i++) {
                    teacherQueue.add(assignment.getTeacher());
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
