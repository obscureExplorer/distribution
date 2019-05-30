package domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by xcy on 2019/5/20.
 */
public class EduClass implements Serializable {
    private static final long serialVersionUID = 2490355394270166676L;

    private Integer id;
    private String name;
    private List<Student> students;
    private String subjectName;
    //0--行政班，1--选考的教学班，2--学考的教学班
    private int type;

    //展示结果用的
    private int index;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "<" + name + ">";
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EduClass eduClass = (EduClass) o;
        return type == eduClass.type &&
                Objects.equals(name, eduClass.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
