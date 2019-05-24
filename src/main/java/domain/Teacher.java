package domain;

/**
 * Created by xcy on 2019/5/20.
 */
public class Teacher  implements Comparable<Teacher>{
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
}
