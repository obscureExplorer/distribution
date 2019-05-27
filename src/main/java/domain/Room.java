package domain;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * Created by xcy on 2019/5/20.
 */
public class Room implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "教室" + "[", "]")
                .add("name='" + name + "'")
                .toString();
    }
}
