package move;

import domain.LectureOfEduClass;
import domain.Room;
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
public class MultipleRoomChangeMove extends AbstractMove<TimeTablingProblem> {

    private List<LectureOfEduClass> lectures;
    private Room toRoom;

    public MultipleRoomChangeMove(List<LectureOfEduClass> lectures, Room toRoom) {
        this.lectures = lectures;
        this.toRoom = toRoom;
    }

    @Override
    protected AbstractMove<TimeTablingProblem> createUndoMove(ScoreDirector<TimeTablingProblem> scoreDirector) {
        return new MultipleRoomChangeMove(lectures,lectures.get(0).getRoom());
    }

    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector<TimeTablingProblem> scoreDirector) {
        for (LectureOfEduClass lecture : lectures) {
            scoreDirector.beforeVariableChanged(lecture, "room");
            lecture.setRoom(toRoom);
            scoreDirector.afterVariableChanged(lecture, "room");
        }
    }

    @Override
    public boolean isMoveDoable(ScoreDirector<TimeTablingProblem> scoreDirector) {
        return !Objects.equals(lectures.get(0).getRoom(), toRoom);
    }

    @Override
    public Move<TimeTablingProblem> rebase(ScoreDirector<TimeTablingProblem> destinationScoreDirector) {
        return new MultipleRoomChangeMove(rebaseList(lectures, destinationScoreDirector),
                destinationScoreDirector.lookUpWorkingObject(toRoom));
    }

    @Override
    public Collection<?> getPlanningEntities() {
        return  Collections.singletonList(lectures);
    }

    @Override
    public Collection<?> getPlanningValues() {
        return Arrays.asList(toRoom);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MultipleRoomChangeMove that = (MultipleRoomChangeMove) o;

        return new EqualsBuilder()
                .append(lectures, that.lectures)
                .append(toRoom, that.toRoom)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(lectures)
                .append(toRoom)
                .toHashCode();
    }

    @Override
    public String toString() {
        return lectures + "{? ->" + toRoom + "}";
    }

}