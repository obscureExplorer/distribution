package domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by xcy on 2019/5/20.
 */
public class Teacher  implements Comparable<Teacher>, Serializable {
    private static final long serialVersionUID = -3540978721456622358L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Teacher o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "<" +
                "'" + name + '\'' +
                '>';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(name, teacher.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
