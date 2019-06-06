package move.factory;

import domain.TimeTablingProblem;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

import java.util.List;

/**
 * Created by xcy on 2019/6/6.
 */
public class ChangeMoveFactory implements MoveListFactory<TimeTablingProblem> {


    @Override
    public List<? extends Move<TimeTablingProblem>> createMoveList(TimeTablingProblem timeTablingProblem) {
        return null;
    }
}
