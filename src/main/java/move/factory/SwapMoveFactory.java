package move.factory;

import domain.TimeTablingProblem;
import org.optaplanner.core.impl.heuristic.move.CompositeMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcy on 2019/6/10.
 */
public class SwapMoveFactory implements MoveListFactory<TimeTablingProblem> {

    @Override
    public List<? extends Move<TimeTablingProblem>> createMoveList(TimeTablingProblem timeTablingProblem) {
        // The create the move list
        List<Move<TimeTablingProblem>> moveList = new ArrayList<>();
        //List<EmployeeMultipleChangeMove> moveListByPillarPartDuo = new ArrayList<>();

        //moveList.add(CompositeMove.buildMove(moveListByPillarPartDuo));
        return moveList;
    }
}
