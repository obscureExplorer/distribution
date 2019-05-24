package filter;

import domain.Lecture;
import domain.TimeTablingProblem;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Created by xcy on 2019/5/21.
 */
public class DifferentCourseSwapMoveFilter implements SelectionFilter<TimeTablingProblem, SwapMove> {
    @Override
    public boolean accept(ScoreDirector<TimeTablingProblem> scoreDirector, SwapMove move) {
        Lecture leftLecture = (Lecture) move.getLeftEntity();
        Lecture rightLecture = (Lecture) move.getRightEntity();
        return !leftLecture.getCourse().equals(rightLecture.getCourse());
    }
}
