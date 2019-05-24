import domain.Course;
import domain.Day;
import domain.EduClass;
import domain.Lecture;
import domain.Student;
import domain.Teacher;
import domain.TimeTablingProblem;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcy on 2019/5/15.
 */
public class Main {
    public static void main(String[] args) {
        // Build the Solver
        SolverFactory<TimeTablingProblem> solverFactory = SolverFactory.createFromXmlResource("config/solverConfig.xml");
        Solver<TimeTablingProblem> solver = solverFactory.buildSolver();
        // TODO: 2019/5/15 查询数据库，获取数据
        TimeTablingProblem problem = new TimeTablingProblem();
        String[] names = {"a","b","c","d"};
        //所有学生
        List<Student> allStudents = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            Student s = new Student();
            s.setId(i);
            s.setName(names[i]);
            allStudents.add(s);
        }
        //所有班级
        List<EduClass> allClasses = new ArrayList<>();
        for(int i = 0; i < 8; ++i){
            EduClass c = new EduClass();
            c.setId(i);
            List<Student> s = new ArrayList<>();
            switch (i){
                case 0: case 2:{
                    s.add(allStudents.get(0));
                    s.add(allStudents.get(1));
                    break;
                }
                case 1: case 5:{
                    s.add(allStudents.get(2));
                    s.add(allStudents.get(3));
                    break;
                }
                case 3: case 4:{
                    s.add(allStudents.get(0));
                    s.add(allStudents.get(2));
                    break;
                }
                case 6: case 7:{
                    s.add(allStudents.get(1));
                    s.add(allStudents.get(3));
                    break;
                }
            }
            c.setStudents(s);
            allClasses.add(c);
        }
        //分班的数据
        problem.setEduClassList(allClasses);
        List<Day> days = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Day d = new Day();
            d.setDayIndex(i);
            days.add(d);
        }
        //创建课程数据
        List<Course> courseList = new ArrayList<>();
        String[] subjectNames = {"语文", "数学", "英语", "数学", "英语", "语文", "物理","化学","生物", "历史","政治","地理"};
        String[] teacherNames = {"张","王","黄","王","黄","张","赵","钱","孙","李","冯","陈"};
        int[] classNos = {1,1,1,2,2,2,1,1,1,1,1,1};
        for (int i = 0; i < subjectNames.length; i++) {
            Course c = new Course();
            c.setName(subjectNames[i]);
            Teacher t = new Teacher();
            t.setName(teacherNames[i]);
            c.setTeacher(t);
            c.setLectureSize(2);
            c.setClassNo(classNos[i]);
            courseList.add(c);
        }
        problem.setCourseList(courseList);
        //lectures数据
        List<Lecture> lectures = new ArrayList<>();
        for (Course course : courseList) {
            for (int i = 0; i < course.getLectureSize(); i++) {
                Lecture lecture = new Lecture();
                lecture.setCourse(course);
                lectures.add(lecture);
            }
        }
        problem.setLectureList(lectures);

        TimeTablingProblem solvedProblem = solver.solve(problem);


    }
}
