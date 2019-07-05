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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xcy on 2019/6/6.
 */
public class MultipleChangeMoveFactory implements MoveListFactory<TimeTablingProblem> {


    //分配老师
    @Override
    public List<? extends Move<TimeTablingProblem>> createMoveList(TimeTablingProblem timeTablingProblem) {
        List<MultipleTeacherChangeMove> moveList = new ArrayList<>();
        List<LectureOfEduClass> lectures = timeTablingProblem.getLectureList();
        Map<Integer, Map<Subject, List<Teacher>>> subjectMap = timeTablingProblem.getSubjectMap();
        Map<Subject, Map<EduClass, List<LectureOfEduClass>>> lectureMap = lectures.stream().collect(Collectors.groupingBy(LectureOfEduClass::getSubject, Collectors.groupingBy(LectureOfEduClass::getEduClass)));

        for (Integer type : subjectMap.keySet()) {
            for (Subject subject : subjectMap.get(type).keySet()) {
                for (Teacher teacher : subjectMap.get(type).get(subject)) {
                    for (EduClass eduClass : lectureMap.get(subject).keySet()) {
                        List<LectureOfEduClass> lectureOfEduClasses = lectureMap.get(subject).get(eduClass);
                        MultipleTeacherChangeMove move = new MultipleTeacherChangeMove(lectureOfEduClasses,teacher);
                        moveList.add(move);
                    }
                }
            }
        }
        return moveList;
    }
}
