package accumulatefunction;

import domain.Course;
import domain.Lecture;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xcy on 2019/5/23.
 */
public class ClassCourseCheck implements org.kie.api.runtime.rule.AccumulateFunction<ClassCourseCheck.CourseCheckData> {

    public static class CourseCheckData implements Serializable{
        protected Map<Object,Object> map;

        public CourseCheckData() {
            this.map = new HashMap<>();
        }
    }

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
    }

    @Override
    public void reverse(CourseCheckData courseCheckData, Object o) throws Exception {

    }

    /**
     * 输入：
     *  (语文,张,1)--1
     *  (语文,张,2)--2
     *  (语文,王,1)--3
     *  期待的排序结果：
     * (语文,王,1)--3
     * (语文,张,2)--2
     * (语文,张,1)--1
     */
    @Override
    public Object getResult(CourseCheckData courseCheckData) throws Exception {
        Map<Object, Object> map = courseCheckData.map;
        List<Course> courses = new ArrayList<>();
        map.keySet().forEach(t -> courses.add((Course) t));
        courses.sort(Course::compareTo);
        boolean startFlag;
        int score = 0;
        Course last = null;
        for (Course c : courses) {
            if(last != null && c.getName().equals(last.getName()))
                startFlag = false;
            else{
                startFlag = true;
            }
            //该课程目前已经分配的课时数
            int size = (int) map.get(c);
            if(startFlag){
                score -= c.getLectureSize() - size;
            }else{
                score -= size;
            }
            last = c;
        }
        return score;
    }

    @Override
    public boolean supportsReverse() {
        return false;
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
}
