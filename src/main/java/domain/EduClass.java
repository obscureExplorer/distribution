package domain;

import java.util.List;

/**
 * Created by xcy on 2019/5/20.
 */
public class EduClass {
    private Integer id;
    //类型为教学班时，所对应的科目
    private Subject subject;
    private List<Student> students;
    //0--行政班，1--选考的教学班，2--学考的教学班
    private int type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
