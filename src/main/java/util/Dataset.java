package util;

import domain.Course;
import domain.Day;
import domain.EduClass;
import domain.Lecture;
import domain.Period;
import domain.Room;
import domain.Student;
import domain.Teacher;
import domain.TimeTablingProblem;
import domain.Timeslot;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by xcy on 2019/5/30.
 */
public class Dataset {

    public static void createDataset(TimeTablingProblem problem,String timeAndRoomFilePath,String classDistributionFilePath,String resourceFilePath) throws IOException {
        //设置时间地点
        InputStreamReader in = new InputStreamReader(new FileInputStream(timeAndRoomFilePath), "gbk");
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        CSVRecord record = records.iterator().next();
        int dayNum = Integer.parseInt(record.get("day"));
        int timeslotNum = Integer.parseInt(record.get("timeslot"));
        int roomNum = Integer.parseInt(record.get("room"));

        List<Day> dayList = IntStream.range(0, dayNum).mapToObj(i -> {
            Day d = new Day();
            d.setDayIndex(i + 1);
            return d;
        }).collect(Collectors.toList());

        List<Timeslot> timeslots = IntStream.range(0, timeslotNum).mapToObj(i -> {
            Timeslot ts = new Timeslot();
            ts.setTimeslotIndex(i + 1);
            return ts;
        }).collect(Collectors.toList());

        List<Period> periodList = new ArrayList<>();
        for (int i = 0; i < dayNum; i++) {
            for (int j = 0; j < timeslotNum; j++) {
                Period p = new Period();
                p.setDay(dayList.get(i));
                p.setTimeslot(timeslots.get(j));
                periodList.add(p);
            }
        }
        List<Room> rooms = IntStream.range(0, roomNum).mapToObj(i -> {
            Room a = new Room();
            a.setName("room" + (i + 1));
            return a;
        }).collect(Collectors.toList());


        problem.setDayList(dayList);
        problem.setTimeslotList(timeslots);
        problem.setPeriodList(periodList);
        problem.setRoomList(rooms);

        //设置课程数据
        in = new InputStreamReader(new FileInputStream(resourceFilePath), "gbk");
        records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        List<Course> courseList = new ArrayList<>();

        //设置课程和班级之间的对应关系
        //行政班——语数英
        //物理教学班——物理
        Map<String, List<EduClass>> relationMap = new HashMap<>();

        for (CSVRecord csvRecord : records) {
            String subjectName = csvRecord.get("subjectName");
            String teacherName = csvRecord.get("teacherName");
            relationMap.putIfAbsent(subjectName, new ArrayList<>());

            int classNo = Integer.parseInt(csvRecord.get("classNo"));
            int lectureSize = Integer.parseInt(csvRecord.get("lectureSize"));
            int type = Integer.parseInt(csvRecord.get("type"));
            Course c = new Course();
            c.setName(subjectName);
            Teacher teacher = new Teacher();
            teacher.setName(teacherName);
            c.setTeacher(teacher);
            c.setLectureSize(lectureSize);
            c.setClassNo(classNo);
            c.setType(type);
            courseList.add(c);
        }
        problem.setCourseList(courseList);

        //构造lectures数据
        int l = 0;
        List<Lecture> lectures = new ArrayList<>();
        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
            for (int j = 0; j < course.getLectureSize(); j++) {
                Lecture lecture = new Lecture();
                lecture.setId((long) (l++));
                lecture.setLectureIndexInCourse(j + 1);
                lecture.setCourse(course);
                lectures.add(lecture);
            }
        }
        problem.setLectureList(lectures);


        //设置分班数据
        in = new InputStreamReader(new FileInputStream(classDistributionFilePath), "gbk");
        records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        Map<EduClass, List<Student>> classMap = new HashMap<>();
        for (CSVRecord csvRecord : records) {
            String className = csvRecord.get("className");
            int type = Integer.parseInt(csvRecord.get("type"));
            String subjectName = csvRecord.get("subjectName");

            EduClass eduClass = new EduClass();
            eduClass.setName(className);
            eduClass.setType(type);
            eduClass.setSubjectName(subjectName);
            String studentName = csvRecord.get("studentName");
            Student s = new Student();
            s.setName(studentName);
            if (!classMap.containsKey(eduClass)) {
                classMap.put(eduClass, new ArrayList<>());
            }
            classMap.get(eduClass).add(s);
        }
        List<EduClass> eduClassList = classMap.keySet().stream().map(key -> {
                    key.setStudents(classMap.get(key));
                    return key;
                }
        ).collect(Collectors.toList());
        problem.setEduClassList(eduClassList);

        //大于0的都是教学班
        eduClassList.stream().filter(c -> c.getType() > 0).forEach(c -> relationMap.get(c.getSubjectName()).add(c));
        List<EduClass> administrativeClass = eduClassList.stream().filter(c -> c.getType() == 0).collect(Collectors.toList());
        relationMap.keySet().stream().filter(key -> relationMap.get(key).size() == 0).forEach(key -> {
            relationMap.put(key, administrativeClass);
        });

        courseList.forEach(t -> t.setEduClassListMap(relationMap));
    }
}
