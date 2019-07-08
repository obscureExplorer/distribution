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
public class MultipleRoomSwapMove extends AbstractMove<TimeTablingProblem> {

    private Room fromRoom;
    private List<LectureOfEduClass> lectures;
    private Room toRoom;

    public MultipleRoomSwapMove(Room fromRoom, List<LectureOfEduClass> lectures, Room toRoom) {
        this.fromRoom = fromRoom;
        this.lectures = lectures;
        this.toRoom = toRoom;
    }

    @Override
    protected AbstractMove<TimeTablingProblem> createUndoMove(ScoreDirector<TimeTablingProblem> scoreDirector) {
        return new MultipleRoomSwapMove(toRoom,lectures,fromRoom);
    }

    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector<TimeTablingProblem> scoreDirector) {
        for (LectureOfEduClass lecture : lectures) {
/*            if(!lecture.getTeacher().equals(fromRoom)){
                throw new IllegalStateException("LectureOfEduClass的room应该都为fromRoom");
            }*/
            scoreDirector.beforeVariableChanged(lecture, "room");
            lecture.setRoom(toRoom);
            scoreDirector.afterVariableChanged(lecture, "room");
        }
    }

    @Override
    public boolean isMoveDoable(ScoreDirector<TimeTablingProblem> scoreDirector) {
        return !Objects.equals(fromRoom, toRoom);
    }

    @Override
    public Move<TimeTablingProblem> rebase(ScoreDirector<TimeTablingProblem> destinationScoreDirector) {
        return new MultipleRoomSwapMove(destinationScoreDirector.lookUpWorkingObject(fromRoom),
                rebaseList(lectures, destinationScoreDirector),
                destinationScoreDirector.lookUpWorkingObject(toRoom));
    }

    @Override
    public Collection<?> getPlanningEntities() {
        return  Collections.singletonList(lectures);
    }

    @Override
    public Collection<?> getPlanningValues() {
        return Arrays.asList(fromRoom, toRoom);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MultipleRoomSwapMove that = (MultipleRoomSwapMove) o;

        return new EqualsBuilder()
                .append(fromRoom, that.fromRoom)
                .append(lectures, that.lectures)
                .append(toRoom, that.toRoom)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(fromRoom)
                .append(lectures)
                .append(toRoom)
                .toHashCode();
    }

    @Override
    public String toString() {
        return lectures + "{? ->" + toRoom + "}";
    }

}