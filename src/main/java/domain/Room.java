package domain;

import java.io.Serializable;

/**
 * Created by xcy on 2019/5/20.
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 3169438635430425998L;

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
}
