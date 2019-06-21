package domain;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by xcy on 2019/5/20.
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 3169438635430425998L;

    @PlanningId
    private Long id;
    private String name;
    //展示结果用的
    private int index;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
