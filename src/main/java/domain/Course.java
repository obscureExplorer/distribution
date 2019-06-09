package domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 课程 -- 三元组(科目，老师，开班号) 或者 二元组(科目，老师）？
 * Created by xcy on 2019/5/20.
 */
public class Course implements Comparable<Course>, Serializable {

    private static final long serialVersionUID = 7252799649193045984L;

    //名称
    private String name;
    //教师
    private Teacher teacher;
    //开班序号
    private int classNo;
    //课时数
    private int lectureSize;
    //0必修，1选考，2学考
    private int type;

    private Map<String, List<EduClass>> eduClassListMap;

    public Map<String, List<EduClass>> getEduClassListMap() {
        return eduClassListMap;
    }

    public void setEduClassListMap(Map<String, List<EduClass>> eduClassListMap) {
        this.eduClassListMap = eduClassListMap;
    }

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
        String type;
        switch (this.type) {
            case 1:
                type = "选考";
                break;
            case 2:
                type = "会考";
                break;
            default:
                type = "行政班";
        }
        return name + '-' + teacher + "-" + lectureSize + "(课时数)-" + classNo + "(开班号)" + "-" + type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        //return classNo == course.classNo &&
        return type == course.type &&
                Objects.equals(name, course.name) &&
                Objects.equals(teacher, course.teacher);
    }

    @Override
    public int hashCode() {
       // return Objects.hash(name, teacher, classNo);
        return Objects.hash(name, teacher,type);
    }

    @Override
    public int compareTo(Course o) {
        if (!this.name.equals(o.name))
            return this.name.compareTo(o.name);
        if (!this.teacher.equals(o.teacher))
            return this.teacher.compareTo(o.teacher);
/*        if (this.classNo != o.classNo)
            return this.classNo - o.classNo;*/
        if (this.type != o.type)
            return this.type - o.type;
        return 0;
    }

    public List<EduClass> getPossibleEduClassList() {
       return eduClassListMap.get(type != 2 ? this.name : "会考" + this.name);
      //  return eduClassListMap.get(this.name);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(name, course.name) &&
                Objects.equals(teacher, course.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, teacher);
    }

    @Override
    public int compareTo(Course o) {
        if(!this.name.equals(o.name))
            return this.name.compareTo(o.name);
        if(!this.teacher.equals(o.teacher))
            return this.teacher.compareTo(o.teacher);
        return 0;
    }*/
}