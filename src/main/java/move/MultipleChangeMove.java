package move;

import domain.TimeTablingProblem;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Created by xcy on 2019/6/10.
 */
public class MultipleChangeMove extends AbstractMove<TimeTablingProblem> {
    @Override
    protected AbstractMove<TimeTablingProblem> createUndoMove(ScoreDirector<TimeTablingProblem> scoreDirector) {
        return null;
    }

    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector<TimeTablingProblem> scoreDirector) {

    }

    @Override
    public boolean isMoveDoable(ScoreDirector<TimeTablingProblem> scoreDirector) {
        return false;
    }
}
