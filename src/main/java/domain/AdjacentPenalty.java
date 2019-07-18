package domain;

import org.optaplanner.core.api.domain.lookup.PlanningId;

/**
 * Created by xcy on 2019/7/12.
 */
public class AdjacentPenalty {
    @PlanningId
    private Long id;
    private String subjectName;
    private String name;
    private int count;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
