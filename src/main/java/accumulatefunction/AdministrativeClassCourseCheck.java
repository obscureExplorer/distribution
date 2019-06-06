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
public class AdministrativeClassCourseCheck implements org.kie.api.runtime.rule.AccumulateFunction<AdministrativeClassCourseCheck.CourseCheckData> {

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
     * (语文,张,1)--1
     * (语文,张,2)--2
     * (语文,王,1)--3
     * 期待的排序结果：
     * (语文,王,1)--3
     * (语文,张,2)--2
     * (语文,张,1)--1
     */
    @Override
    public Object getResult(CourseCheckData courseCheckData) throws Exception {
        Map<Object, Object> map = courseCheckData.map;
        //说明该班级没有排课，返回一个更高的惩罚分值
        if(map.size() == 0){
            return -20;
        }
        List<Course> courses = new ArrayList<>();
        map.keySet().forEach(t -> courses.add((Course) t));
        courses.sort(Course::compareTo);
        boolean startFlag;
        int score = 0;
        Course last = null;
        int count = 0;
        for (Course c : courses) {
            if (last != null && c.getName().equals(last.getName()))
                startFlag = false;
            else {
                startFlag = true;
                ++count;
            }
            //该课程目前已经分配的课时数
            int size = (int) map.get(c);
            if (startFlag) {
                int diff = c.getLectureSize() - size;
                score -= diff > 0? diff : -diff;
            } else {
                score -= size;
            }
            last = c;
        }
        //对于行政班--count应该是6
        return score + (count - 6) * 10;
       // return score + (count - 6);
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

        public CourseCheckData() {
            this.map = new HashMap<>();
        }
    }
}
