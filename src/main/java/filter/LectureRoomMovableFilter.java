package filter;

import domain.LectureOfEduClass;
import domain.TimeTablingProblem;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Created by xcy on 2019/6/28.
 */
public class LectureRoomMovableFilter implements SelectionFilter<TimeTablingProblem,LectureOfEduClass> {

    @Override
    public boolean accept(ScoreDirector<TimeTablingProblem> scoreDirector, LectureOfEduClass lectureOfEduClass) {
        return !lectureOfEduClass.isRoomUnmovable();
    }
}
