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
    //0-必修，1-选考，2-学考
    private int type;
    //科目的课时数
    private int lectureSize;

    private Map<Integer, Map<Subject, List<Teacher>>> subjectMap;

    public Subject() {

    }

    public Subject(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public Subject(String name, int type,int lectureSize) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
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
        String typeName;
        switch (type){
            case 0:
                typeName = "必修";
                break;
            case 1:
                typeName = "学考";
                break;
            case 2:
                typeName = "选考";
                break;
            default:
                typeName = "未知";
        }
        return "<" +  name +  "," + typeName + ">";
    }

    public List<Teacher> getPossibleTeacher(){
        return subjectMap.get(type).get(this);
    }

    public Map<Integer, Map<Subject, List<Teacher>>> getSubjectMap() {
        return subjectMap;
    }

    public void setSubjectMap(Map<Integer, Map<Subject, List<Teacher>>> subjectMap) {
        this.subjectMap = subjectMap;
    }

    @Override
    public int compareTo(Subject o) {
        return new CompareToBuilder()
                .append(this.name,o.name)
                .append(this.type,this.type).toComparison();
    }
}
