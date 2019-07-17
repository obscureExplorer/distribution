package move.factory;

import domain.EduClass;
import domain.LectureOfEduClass;
import domain.Subject;
import domain.Teacher;
import domain.TimeTablingProblem;
import move.MultipleTeacherChangeMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xcy on 2019/6/6.
 */
public class MultipleTeacherChangeMoveFactory implements MoveListFactory<TimeTablingProblem> {

    //分配老师
    @Override
    public List<? extends Move<TimeTablingProblem>> createMoveList(TimeTablingProblem timeTablingProblem) {
        List<MultipleTeacherChangeMove> moveList = new ArrayList<>();
        List<LectureOfEduClass> lectures = timeTablingProblem.getLectureList();
        Map<Subject, Collection<Teacher>> subjectTeacherMap = timeTablingProblem.getSubjectTeacherMap();
        //过滤掉老师不能变动的
        Map<Subject, Map<EduClass, List<LectureOfEduClass>>> lectureMap = lectures.stream().filter(l -> !l.isTeacherUnmovable()).collect(Collectors.groupingBy(LectureOfEduClass::getSubject, Collectors.groupingBy(LectureOfEduClass::getEduClass)));

        for (Subject subject : subjectTeacherMap.keySet()) {
            for (Teacher teacher : subjectTeacherMap.get(subject)) {
                for (EduClass eduClass : lectureMap.get(subject).keySet()) {
                    List<LectureOfEduClass> lectureOfEduClasses = lectureMap.get(subject).get(eduClass);
                    MultipleTeacherChangeMove move = new MultipleTeacherChangeMove(lectureOfEduClasses, teacher);
                    moveList.add(move);
                }
            }
        }
        return moveList;
    }
}
