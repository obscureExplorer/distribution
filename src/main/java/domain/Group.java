package domain;

import com.google.common.base.Objects;

/**
 * Created by xcy on 2019/7/18.
 */
public class Group {
    private String name;
    private int subjectPeriodSize;

    public Group(String name, int subjectPeriodSize) {
        this.name = name;
        this.subjectPeriodSize = subjectPeriodSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubjectPeriodSize() {
        return subjectPeriodSize;
    }

    public void setSubjectPeriodSize(int subjectPeriodSize) {
        this.subjectPeriodSize = subjectPeriodSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return subjectPeriodSize == group.subjectPeriodSize &&
                Objects.equal(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, subjectPeriodSize);
    }
}
