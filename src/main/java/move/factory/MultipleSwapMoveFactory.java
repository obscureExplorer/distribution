package move.factory;

import domain.EduClass;
import domain.LectureOfEduClass;
import domain.Subject;
import domain.Teacher;
import domain.TimeTablingProblem;
import move.MultipleSwapMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by xcy on 2019/6/6.
 */
public class MultipleSwapMoveFactory implements MoveListFactory<TimeTablingProblem> {


    //分配老师
    @Override
    public List<? extends Move<TimeTablingProblem>> createMoveList(TimeTablingProblem timeTablingProblem) {
        List<MultipleSwapMove> moveList = new ArrayList<>();
        List<LectureOfEduClass> lectures = timeTablingProblem.getLectureList();
        Map<Subject, Map<EduClass, List<LectureOfEduClass>>> lectureMap = lectures.stream().collect(Collectors.groupingBy(LectureOfEduClass::getSubject, Collectors.groupingBy(LectureOfEduClass::getEduClass)));

        for (Subject subject : lectureMap.keySet()) {
            Set<EduClass> eduClasses = lectureMap.get(subject).keySet();
            Generator.combination(eduClasses).simple(2).stream().forEach(combo -> {
                Teacher t1 = lectureMap.get(subject).get(combo.get(0)).get(0).getTeacher();
                Teacher t2 = lectureMap.get(subject).get(combo.get(1)).get(0).getTeacher();
                if (!t1.equals(t2)) {
                    MultipleSwapMove move1 = new MultipleSwapMove(t1, lectureMap.get(subject).get(combo.get(0)), t2);
                    MultipleSwapMove move2 = new MultipleSwapMove(t2, lectureMap.get(subject).get(combo.get(1)), t1);
                    moveList.add(move1);
                    moveList.add(move2);
                }
            });
        }
        return moveList;
    }
}
