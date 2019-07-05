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
public class MultipleTeacherChangeMove extends AbstractMove<TimeTablingProblem> {

    private List<LectureOfEduClass> lectures;
    private Teacher toTeacher;

    public MultipleTeacherChangeMove(List<LectureOfEduClass> lectures, Teacher toTeacher) {
        this.lectures = lectures;
        this.toTeacher = toTeacher;
    }

    @Override
    protected AbstractMove<TimeTablingProblem> createUndoMove(ScoreDirector<TimeTablingProblem> scoreDirector) {
        return new MultipleTeacherChangeMove(lectures,lectures.get(0).getTeacher());
    }

    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector<TimeTablingProblem> scoreDirector) {
        for (LectureOfEduClass lecture : lectures) {
            scoreDirector.beforeVariableChanged(lecture, "teacher");
            lecture.setTeacher(toTeacher);
            scoreDirector.afterVariableChanged(lecture, "teacher");
        }
    }

    @Override
    public boolean isMoveDoable(ScoreDirector<TimeTablingProblem> scoreDirector) {
        return !Objects.equals(lectures.get(0).getTeacher(), toTeacher);
    }

    @Override
    public Move<TimeTablingProblem> rebase(ScoreDirector<TimeTablingProblem> destinationScoreDirector) {
        return new MultipleTeacherChangeMove(rebaseList(lectures, destinationScoreDirector),
                destinationScoreDirector.lookUpWorkingObject(toTeacher));
    }

    @Override
    public Collection<?> getPlanningEntities() {
        return  Collections.singletonList(lectures);
    }

    @Override
    public Collection<?> getPlanningValues() {
        return Arrays.asList(toTeacher);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MultipleTeacherChangeMove that = (MultipleTeacherChangeMove) o;

        return new EqualsBuilder()
                .append(lectures, that.lectures)
                .append(toTeacher, that.toTeacher)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(lectures)
                .append(toTeacher)
                .toHashCode();
    }

    @Override
    public String toString() {
        return lectures + "{? ->" + toTeacher + "}";
    }

}