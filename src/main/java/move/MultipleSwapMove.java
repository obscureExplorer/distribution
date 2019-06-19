package move;

import domain.LectureOfEduClass;
import domain.Teacher;
import domain.TimeTablingProblem;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by xcy on 2019/6/10.
 */
public class MultipleSwapMove extends AbstractMove<TimeTablingProblem> {

    private Teacher fromTeacher;
    private List<LectureOfEduClass> lectures;
    private Teacher toTeacher;

    public MultipleSwapMove(Teacher fromTeacher, List<LectureOfEduClass> lectures, Teacher toTeacher) {
        this.fromTeacher = fromTeacher;
        this.lectures = lectures;
        this.toTeacher = toTeacher;
    }

    @Override
    protected AbstractMove<TimeTablingProblem> createUndoMove(ScoreDirector<TimeTablingProblem> scoreDirector) {
        return new MultipleSwapMove(toTeacher,lectures,fromTeacher);
    }

    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector<TimeTablingProblem> scoreDirector) {
        for (LectureOfEduClass lecture : lectures) {
            if(!lecture.getTeacher().equals(fromTeacher)){
                throw new IllegalStateException("LectureOfEduClass应该具有相同的teacher作为fromTeacher");
            }
            scoreDirector.beforeVariableChanged(lecture, "teacher");
            lecture.setTeacher(toTeacher);
            scoreDirector.afterVariableChanged(lecture, "teacher");
        }
    }

    @Override
    public boolean isMoveDoable(ScoreDirector<TimeTablingProblem> scoreDirector) {
        return !Objects.equals(fromTeacher, toTeacher);
    }

    @Override
    public Move<TimeTablingProblem> rebase(ScoreDirector<TimeTablingProblem> destinationScoreDirector) {
        return new MultipleSwapMove(destinationScoreDirector.lookUpWorkingObject(fromTeacher),
                rebaseList(lectures, destinationScoreDirector),
                destinationScoreDirector.lookUpWorkingObject(toTeacher));
    }

    @Override
    public Collection<?> getPlanningEntities() {
        return  Collections.singletonList(lectures);
    }

    @Override
    public Collection<?> getPlanningValues() {
        return Arrays.asList(fromTeacher, toTeacher);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MultipleSwapMove that = (MultipleSwapMove) o;

        return new EqualsBuilder()
                .append(fromTeacher, that.fromTeacher)
                .append(lectures, that.lectures)
                .append(toTeacher, that.toTeacher)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(fromTeacher)
                .append(lectures)
                .append(toTeacher)
                .toHashCode();
    }

    @Override
    public String toString() {
        return lectures + "{? ->" + toTeacher + "}";
    }

}