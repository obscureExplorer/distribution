package move.factory;

import domain.EduClass;
import domain.LectureOfEduClass;
import domain.Room;
import domain.TimeTablingProblem;
import move.MultipleRoomChangeMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xcy on 2019/7/8.
 */
public class MultipleRoomChangeMoveFactory implements MoveListFactory<TimeTablingProblem> {

    @Override
    public List<? extends Move<TimeTablingProblem>> createMoveList(TimeTablingProblem timeTablingProblem) {
        List<MultipleRoomChangeMove> moveList = new ArrayList<>();
        List<LectureOfEduClass> lectures = timeTablingProblem.getLectureList();
        Map<EduClass, List<LectureOfEduClass>> eduClasses = lectures.stream()
                .filter(lectureOfEduClass -> !lectureOfEduClass.isRoomUnmovable())
                .collect(Collectors.groupingBy(LectureOfEduClass::getEduClass));
        List<Room> allRoom = timeTablingProblem.getRoomList();
        List<Room> roomList = allRoom.subList(1,allRoom.size());
        for (EduClass eduClass : eduClasses.keySet()) {
            for (Room room : roomList) {
                MultipleRoomChangeMove move = new MultipleRoomChangeMove(eduClasses.get(eduClass),room);
                moveList.add(move);
            }
        }
        return moveList;
    }

}
