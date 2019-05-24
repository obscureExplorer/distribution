package domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 课程 -- 三元组(科目，老师，开班序号)
 * Created by xcy on 2019/5/20.
 */
public class Course implements Comparable<Course> {
    //名称
    private String name;
    //教师
    private Teacher teacher;
    //开班序号
    private int classNo;
    //课时数
    private int lectureSize;

    private Map<String,List<EduClass>> eduClassListMap;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getLectureSize() {
        return lectureSize;
    }

    public void setLectureSize(int lectureSize) {
        this.lectureSize = lectureSize;
    }

    public int getClassNo() {
        return classNo;
    }

    public void setClassNo(int classNo) {
        this.classNo = classNo;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return classNo == course.classNo &&
                Objects.equals(name, course.name) &&
                Objects.equals(teacher, course.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, teacher, classNo);
    }

    @Override
    public int compareTo(Course o) {
        if(!this.name.equals(o.name))
            return this.name.compareTo(o.name);
        if(!this.teacher.equals(o.teacher))
            return this.teacher.compareTo(o.teacher);
        if(this.classNo != o.classNo)
            return this.classNo - o.classNo;
        return 0;
    }

    public List<EduClass> getPossibleEduClassList() {
        return eduClassListMap.get(this.name);
    }
}
