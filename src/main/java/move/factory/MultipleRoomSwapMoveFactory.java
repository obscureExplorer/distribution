package move.factory;

import domain.EduClass;
import domain.LectureOfEduClass;
import domain.Room;
import domain.TimeTablingProblem;
import move.MultipleRoomChangeMove;
import move.MultipleRoomSwapMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by xcy on 2019/7/8.
 */
public class MultipleRoomSwapMoveFactory implements MoveListFactory<TimeTablingProblem> {

    @Override
    public List<? extends Move<TimeTablingProblem>> createMoveList(TimeTablingProblem timeTablingProblem) {
        List<MultipleRoomSwapMove> moveList = new ArrayList<>();
        List<LectureOfEduClass> lectures = timeTablingProblem.getLectureList();
        Map<EduClass, List<LectureOfEduClass>> eduClasses = lectures.stream()
                .filter(lectureOfEduClass -> !lectureOfEduClass.isRoomUnmovable())
                .collect(Collectors.groupingBy(LectureOfEduClass::getEduClass));
        Set<EduClass> eduClassSet = eduClasses.keySet();
        Generator.combination(eduClassSet).simple(2).stream().forEach(combo -> {
            List<LectureOfEduClass> leftEduClasses = eduClasses.get(combo.get(0));
            List<LectureOfEduClass> rightEduClasses = eduClasses.get(combo.get(1));

            LectureOfEduClass leftLecture = leftEduClasses.get(0);
            LectureOfEduClass rightLecture = rightEduClasses.get(0);
            if(!leftLecture.getRoom().equals(rightLecture.getRoom())){
                MultipleRoomSwapMove move = new MultipleRoomSwapMove(leftLecture.getRoom(),leftEduClasses, rightLecture.getRoom());
                MultipleRoomSwapMove move2 = new MultipleRoomSwapMove(rightLecture.getRoom(),rightEduClasses, leftLecture.getRoom());
                moveList.add(move);
                moveList.add(move2);
            }
        });
        return moveList;
    }

}
