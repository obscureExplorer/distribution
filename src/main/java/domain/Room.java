package domain;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * Created by xcy on 2019/5/20.
 */
public class Room implements Serializable {
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
        return "<" + name + ">";
    }
}
