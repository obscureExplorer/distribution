package domain;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by xcy on 2019/6/14.
 */
public class Subject implements Comparable<Subject>{
    //科目名
    private String name;
    //必修、高考、选考
    private SubjectTypeEnum type;
    //科目的课时数
    private int lectureSize;

    private Map<SubjectTypeEnum, Map<Subject, List<Teacher>>> subjectMap;

    public Subject(String name, SubjectTypeEnum type, int lectureSize) {
        this.name = name;
        this.type = type;
        this.lectureSize = lectureSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubjectTypeEnum getType() {
        return type;
    }

    public void setType(SubjectTypeEnum type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return type == subject.type &&
                Objects.equals(name, subject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    public int getLectureSize() {
        return lectureSize;
    }

    public void setLectureSize(int lectureSize) {
        this.lectureSize = lectureSize;
    }

    @Override
    public String toString() {
        return "<" +  name +  "," + type + ">";
    }

    public List<Teacher> getPossibleTeacher(){
        return subjectMap.get(type).get(this);
    }

    public Map<SubjectTypeEnum, Map<Subject, List<Teacher>>> getSubjectMap() {
        return subjectMap;
    }

    public void setSubjectMap(Map<SubjectTypeEnum, Map<Subject, List<Teacher>>> subjectMap) {
        this.subjectMap = subjectMap;
    }

    @Override
    public int compareTo(Subject o) {
        return new CompareToBuilder()
                .append(this.name,o.name)
                .append(this.type,this.type).toComparison();
    }
}
