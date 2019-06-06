package accumulatefunction;

import domain.Course;
import domain.Lecture;
import javafx.util.Pair;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PipedInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xcy on 2019/5/23.
 */
public class ClassCourseCheck implements org.kie.api.runtime.rule.AccumulateFunction<ClassCourseCheck.CourseCheckData> {

    @Override
    public CourseCheckData createContext() {
        return new CourseCheckData();
    }

    @Override
    public void init(CourseCheckData courseCheckData) throws Exception {
    }

    @Override
    public void accumulate(CourseCheckData courseCheckData, Object o) {
        Lecture lecture = (Lecture) o;
        Course course = lecture.getCourse();
        Map<Object, Object> map = courseCheckData.map;
        if (map.containsKey(course)) {
            int num = (int) map.get(course);
            map.put(course, num + 1);
        } else {
            map.put(course, 1);
        }
        if (courseCheckData.lectureSizePerCourse == 0) {
            int type = ((Lecture) o).getEduClass().getType();
            courseCheckData.lectureSizePerCourse = type == 1 ? 4 : 2;
            courseCheckData.classType = type;
        }
    }

    @Override
    public void reverse(CourseCheckData courseCheckData, Object o) throws Exception {
        Lecture lecture = (Lecture) o;
        Course course = lecture.getCourse();
        Map<Object, Object> map = courseCheckData.map;
        int num = (int) map.get(course);
        if (num == 1)
            map.remove(course);
        else
            map.put(course, num - 1);
    }

    /**
     * 输入：
     * (地理，张，1)
     * 期待的排序结果：
     */
    @Override
    public Object getResult(CourseCheckData courseCheckData) throws Exception {
        Map<Object, Object> map = courseCheckData.map;
        //说明该班级没有排课，返回一个更高的惩罚分值
        if(map.size() == 0){
            return -20;
        }

        int score = 0;

        List<Pair<Course, Integer>> pairs = map.keySet().stream().map(k ->
                new Pair<>((Course) k, (int) map.get(k))
        ).collect(Collectors.toList());

        List<Pair<Course,Integer>> right = new ArrayList<>();
        for (Pair<Course, Integer> pair : pairs) {
            Course course = pair.getKey();
            if(course.getType() == courseCheckData.classType){
                right.add(pair);
            }else{
                score += pair.getValue() * 10;
               //score += pair.getValue();
            }
        }

        Collections.sort(right, ((o1, o2) -> o2.getValue() - o1.getValue()));
        for (int i = 0; i < right.size(); i++) {
            if(i == 0){
                int diff = right.get(i).getKey().getLectureSize() - right.get(i).getValue();
                diff = diff > 0 ? diff : -diff;
                score += diff;
            }else {
                score += right.get(i).getValue();
            }
        }
        return -score;
    }

    @Override
    public boolean supportsReverse() {
        return true;
    }

    @Override
    public Class<?> getResultType() {
        return Integer.class;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }

    public static class CourseCheckData implements Serializable {
        protected Map<Object, Object> map;
        private int lectureSizePerCourse;
        private int classType;

        public CourseCheckData() {
            this.map = new HashMap<>();
        }
    }
}
