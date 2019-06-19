package filter;

import domain.LectureOfEduClass;
import domain.TimeTablingProblem;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Objects;

/**
 * Created by xcy on 2019/5/21.
 */
public class DifferentTeacherSwapMoveFilter implements SelectionFilter<TimeTablingProblem, SwapMove> {

    //交换同一个课程的不同课时，对分值没有影响。所以需要过滤掉它。
    @Override
    public boolean accept(ScoreDirector<TimeTablingProblem> scoreDirector, SwapMove move) {
        LectureOfEduClass leftLecture = (LectureOfEduClass) move.getLeftEntity();
        LectureOfEduClass rightLecture = (LectureOfEduClass) move.getRightEntity();
        return !Objects.equals(leftLecture.getTeacher(),rightLecture.getTeacher());
    }
}
