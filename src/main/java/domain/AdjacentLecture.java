package domain;

/**
 * Created by xcy on 2019/7/16.
 */
public class AdjacentLecture {
    private String subjectName;
    private Teacher teacher;

    public AdjacentLecture(String subjectName, Teacher teacher) {
        this.subjectName = subjectName;
        this.teacher = teacher;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
