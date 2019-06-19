package util;

import domain.Day;
import domain.EduClass;
import domain.LectureOfEduClass;
import domain.Period;
import domain.Room;
import domain.Student;
import domain.Subject;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by xcy on 2019/5/30.
 */
public class Dataset {

    public static void createDataset(TimeTablingProblem problem, String parentFolder) throws IOException {
        //设置时间地点
        InputStreamReader in = new InputStreamReader(new FileInputStream(parentFolder + "/时间地点.csv"), "gbk");
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
        in = new InputStreamReader(new FileInputStream(parentFolder + "/教学资源.csv"), "gbk");
        records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

        //最终效果：
        //必修 -- (语文,必修) -- [张，黄，陈]
        //     -- (体育,必修) -- [蒋]
        //选考 --(物理，学考) -- [王，李]
        //     --(化学，学考) -- [季]
        //会考 --(政治，会考) -- [魏]
        //     --(生物，会考) -- [林]
        Map<Integer, Map<Subject, List<Teacher>>> subjectMap = new HashMap<>();
        Set<Teacher> teacherSet = new HashSet<>();

        for (CSVRecord csvRecord : records) {
            String subjectName = csvRecord.get("subjectName");
            String teacherName = csvRecord.get("teacherName");
            int maxClassNum = Integer.parseInt(csvRecord.get("classNo"));
            int type = Integer.parseInt(csvRecord.get("type"));
            int lectureSize = Integer.parseInt(csvRecord.get("lectureSize"));

            Subject subject = new Subject(subjectName, type, lectureSize);
            subject.setSubjectMap(subjectMap);

            Teacher teacher = new Teacher(teacherName, maxClassNum);
            teacherSet.add(teacher);
            subjectMap.putIfAbsent(type, new HashMap<>());
            subjectMap.get(type).putIfAbsent(subject, new ArrayList<>());
            subjectMap.get(type).get(subject).add(teacher);
        }
        problem.setSubjectMap(subjectMap);

        //设置分班数据
        in = new InputStreamReader(new FileInputStream(parentFolder + "/分班数据.csv"), "gbk");
        records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        Map<EduClass, List<Student>> classStudentMap = new HashMap<>();
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
            if (!classStudentMap.containsKey(eduClass)) {
                classStudentMap.put(eduClass, new ArrayList<>());
            }
            classStudentMap.get(eduClass).add(s);
        }
        List<EduClass> eduClassList = classStudentMap.keySet().stream().peek(key -> key.setStudents(classStudentMap.get(key)))
                .collect(Collectors.toList());
        problem.setEduClassList(eduClassList);

        Map<Integer, Map<Subject, List<EduClass>>> classMap = new HashMap<>();
        for (EduClass eduClass : eduClassList) {
            int type = eduClass.getType();
            String subjectName = eduClass.getSubjectName();

            classMap.putIfAbsent(type,new HashMap<>());
            Subject subject = new Subject(subjectName,type);
            classMap.get(type).putIfAbsent(subject,new ArrayList<>());
            classMap.get(type).get(subject).add(eduClass);
        }
        //构造lectures数据--假定行政班对应必修课，学考班对应学考科目，会考班对应会考科目
        List<LectureOfEduClass> lectureOfEduClasses = new ArrayList<>();
        long k = 0;
        for (EduClass eduClass : eduClassList) {
            int type = eduClass.getType();
            Map<Subject, List<Teacher>> subjects = subjectMap.get(type);
            if(type == 0){
                for (Subject subject : subjects.keySet()) {
                    int lectureSize = subject.getLectureSize();
                    for (int i = 0; i < lectureSize; i++) {
                        LectureOfEduClass lecture = new LectureOfEduClass();
                        lecture.setId(k++);
                        lecture.setEduClass(eduClass);
                        lecture.setLectureIndex(i);
                        lecture.setSubject(subject);
                        lectureOfEduClasses.add(lecture);
                    }
                }
            }else{
                for (Subject subject : subjects.keySet()) {
                    if(subject.getName().equals(eduClass.getSubjectName())){
                        int size = subject.getLectureSize();
                        for (int i = 0; i < size; i++) {
                            LectureOfEduClass lecture = new LectureOfEduClass();
                            lecture.setId(k++);
                            lecture.setEduClass(eduClass);
                            lecture.setLectureIndex(i);
                            lecture.setSubject(subject);
                            lectureOfEduClasses.add(lecture);
                        }
                        break;
                    }
                }
            }
        }
        problem.setLectureList(lectureOfEduClasses);

        List<Teacher> teacherList = new ArrayList<>(teacherSet);
        problem.setTeacherList(teacherList);


    }
}
