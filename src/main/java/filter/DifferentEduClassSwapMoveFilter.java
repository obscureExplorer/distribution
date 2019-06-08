package filter;

import domain.RealEduClass;
import domain.TimeTablingProblem;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Created by xcy on 2019/5/21.
 */
public class DifferentEduClassSwapMoveFilter implements SelectionFilter<TimeTablingProblem, SwapMove> {

    //交换同一个课程的不同课时，对分值没有影响。所以需要过滤掉它。
    @Override
    public boolean accept(ScoreDirector<TimeTablingProblem> scoreDirector, SwapMove move) {
        RealEduClass left= (RealEduClass) move.getLeftEntity();
        RealEduClass right = (RealEduClass) move.getRightEntity();
        return !left.getEduClass().equals(right.getEduClass());
    }
}
