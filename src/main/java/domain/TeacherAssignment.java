package domain;

/**
 * Created by xcy on 2019/7/17.
 */
public class TeacherAssignment {
    private Subject subject;
    private Teacher teacher;
    private int maxClassNo;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getMaxClassNo() {
        return maxClassNo;
    }

    public void setMaxClassNo(int maxClassNo) {
        this.maxClassNo = maxClassNo;
    }
}
