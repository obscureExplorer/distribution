package util;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import domain.Day;
import domain.EduClass;
import domain.EduClassTypeEnum;
import domain.Group;
import domain.GroupEduClass;
import domain.LectureOfEduClass;
import domain.Period;
import domain.ResourceTypeEnum;
import domain.Room;
import domain.RuleTypeEnum;
import domain.Student;
import domain.Subject;
import domain.SubjectTypeEnum;
import domain.Teacher;
import domain.TeacherAssignment;
import domain.TimeTablingProblem;
import domain.Timeslot;
import domain.PeriodPenalty;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
            d.setId((long) i);
            d.setDayIndex(i + 1);
            return d;
        }).collect(Collectors.toList());

        List<Timeslot> timeslots = IntStream.range(0, timeslotNum).mapToObj(i -> {
            Timeslot ts = new Timeslot();
            ts.setId((long) i);
            ts.setTimeslotIndex(i + 1);
            return ts;
        }).collect(Collectors.toList());

        long id = 0;
        List<Period> periodList = new ArrayList<>();
        for (int i = 0; i < dayNum; i++) {
            for (int j = 0; j < timeslotNum; j++) {
                Period p = new Period();
                p.setId(id++);
                p.setDay(dayList.get(i));
                p.setTimeslot(timeslots.get(j));
                periodList.add(p);
            }
        }
        List<Room> rooms = IntStream.range(0, roomNum).mapToObj(i -> {
            Room a = new Room();
            a.setId((long) i);
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

        // 设置每个科目对应的老师
        // 最终的结果：
        //(语文,必修) -- [张，黄，陈]
        //(物理，学考) -- [王，李]
        //(化学，学考) -- [季]
        //(政治，会考) -- [魏]
        //(生物，会考) -- [林]
        Multimap<Subject, Teacher> subjectMultimap = LinkedHashMultimap.create();
        Map<String, Teacher> teacherMap = new LinkedHashMap<>();
        id = 0;
        List<TeacherAssignment> teacherAssignments = new ArrayList<>();
        for (CSVRecord csvRecord : records) {
            String subjectName = csvRecord.get("subjectName");
            String teacherName = csvRecord.get("teacherName");
            int maxClassNum = Integer.parseInt(csvRecord.get("classNo"));
            String typeName = csvRecord.get("type");
            SubjectTypeEnum type;
            switch (typeName) {
                case "必修":
                    type = SubjectTypeEnum.OBLIGATORY;
                    break;
                case "会考":
                    type = SubjectTypeEnum.ACADEMIC;
                    break;
                default:
                    type = SubjectTypeEnum.COLLEDGE;
                    break;
            }
            int lectureSize = Integer.parseInt(csvRecord.get("lectureSize"));
            Subject currentSubject = new Subject(subjectName, type, lectureSize);
            Teacher currentTeacher;
            if (!teacherMap.containsKey(teacherName)) {
                currentTeacher = new Teacher(teacherName);
                currentTeacher.setId(id++);
                teacherMap.put(teacherName, currentTeacher);
            } else {
                currentTeacher = teacherMap.get(teacherName);
            }
            subjectMultimap.put(currentSubject, currentTeacher);
            TeacherAssignment teacherAssignment = new TeacherAssignment();
            teacherAssignment.setSubject(currentSubject);
            teacherAssignment.setTeacher(currentTeacher);
            teacherAssignment.setMaxClassNo(maxClassNum);
            teacherAssignments.add(teacherAssignment);
        }
        Map<Subject, Collection<Teacher>> subjectTeacherMap = subjectMultimap.asMap();
        problem.setSubjectTeacherMap(subjectTeacherMap);
        problem.setTeacherAssignmentList(teacherAssignments);

        //设置所有的科目
        List<Subject> subjectList = subjectTeacherMap.keySet().stream().collect(Collectors.toList());
        problem.setSubjectList(subjectList);
        //设置所有的老师
        List<Teacher> teacherList = teacherMap.keySet().stream().map(k -> teacherMap.get(k)).collect(Collectors.toList());
        problem.setTeacherList(teacherList);

        //设置分班数据
        id = 0;
        in = new InputStreamReader(new FileInputStream(parentFolder + "/分班数据.csv"), "gbk");
        records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        Map<EduClass, List<Student>> classStudentMap = new LinkedHashMap<>();
        for (CSVRecord csvRecord : records) {
            String typeName = csvRecord.get("type");
            EduClassTypeEnum type;
            switch (typeName) {
                case "行政班":
                    type = EduClassTypeEnum.ADMINISTRATIVE;
                    break;
                case "会考班":
                    type = EduClassTypeEnum.ACADEMIC;
                    break;
                case "高考班":
                    type = EduClassTypeEnum.COLLEGE;
                    break;
                default:
                    type = EduClassTypeEnum.GROUP;
                    break;
            }
            String className = csvRecord.get("className");
            EduClass eduClass = new EduClass();
            eduClass.setId(id++);
            eduClass.setName(className);
            eduClass.setType(type);
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

        // 读入班级和科目的对应关系
        in = new InputStreamReader(new FileInputStream(parentFolder + "/班级科目对应关系.csv"), "gbk");
        records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        Multimap<String, Subject> eduClassSubjectMultimap = LinkedHashMultimap.create();
        for (CSVRecord csvRecord : records) {
            String typeName = csvRecord.get("type");
            SubjectTypeEnum type;
            switch (typeName) {
                case "必修":
                    type = SubjectTypeEnum.OBLIGATORY;
                    break;
                case "会考":
                    type = SubjectTypeEnum.ACADEMIC;
                    break;
                default:
                    type = SubjectTypeEnum.COLLEDGE;
                    break;
            }
            int lectureSize = Integer.parseInt(csvRecord.get("lectureSize"));
            Subject subject = new Subject(csvRecord.get("subject"), type, lectureSize);
            eduClassSubjectMultimap.put(csvRecord.get("eduClass"), subject);
        }

        //构造lectures数据
        List<LectureOfEduClass> lectureOfEduClasses = new ArrayList<>();
        Iterator<Room> roomIterator = rooms.iterator();
        id = 0;
        for (EduClass eduClass : eduClassList) {
            Collection<Subject> subjectsOfEduClass = eduClassSubjectMultimap.get(eduClass.getName());
            //行政班固定教室
            Room room;
            boolean roomUnmovable;
            if (eduClass.getType() == EduClassTypeEnum.ADMINISTRATIVE) {
                room = roomIterator.next();
                roomUnmovable = true;
            } else {
                room = null;
                roomUnmovable = false;
            }
            for (Subject subject : subjectsOfEduClass) {
                for (int i = 0; i < subject.getLectureSize(); i++) {
                    LectureOfEduClass lecture = new LectureOfEduClass();
                    lecture.setId(id++);
                    lecture.setEduClass(eduClass);
                    lecture.setLectureIndex(i);
                    lecture.setSubject(subject);
                    lecture.setRoom(room);
                    lecture.setRoomUnmovable(roomUnmovable);
                    lectureOfEduClasses.add(lecture);
                }
            }
        }
        problem.setLectureList(lectureOfEduClasses);

        //读取不可用的时间信息
        if (Files.exists(Paths.get(parentFolder + "/可用与不可用时间.csv"))) {
            //设置可用与不可用时间
            in = new InputStreamReader(new FileInputStream(parentFolder + "/可用与不可用时间.csv"), "gbk");
            records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<PeriodPenalty> periodPenaltyList = new ArrayList<>();
            id = 0;
            for (CSVRecord csvRecord : records) {
                PeriodPenalty periodPenalty = new PeriodPenalty();
                periodPenalty.setId(++id);
                periodPenalty.setName(csvRecord.get("name"));
                periodPenalty.setSubjectName(csvRecord.get("subjectName"));
                Day day = new Day();
                day.setDayIndex(Integer.parseInt(csvRecord.get("day")));
                Timeslot timeslot = new Timeslot();
                timeslot.setTimeslotIndex(Integer.parseInt(csvRecord.get("timeslot")));
                Period period = new Period();
                period.setDay(day);
                period.setTimeslot(timeslot);
                periodPenalty.setPeriod(period);

                if ("班级".equals(csvRecord.get("resourceType"))) {
                    periodPenalty.setResourceType(ResourceTypeEnum.EDUCLASS);
                } else if ("老师".equals(csvRecord.get("resourceType"))) {
                    periodPenalty.setResourceType(ResourceTypeEnum.TEACHER);
                } else {
                    throw new UnsupportedOperationException();
                }

                if ("不可用".equals(csvRecord.get("ruleType"))) {
                    periodPenalty.setRuleType(RuleTypeEnum.UNAVAILABILITY);
                } else if ("可用".equals(csvRecord.get("ruleType"))) {
                    periodPenalty.setRuleType(RuleTypeEnum.AVAILABILITY);
                } else {
                    throw new UnsupportedOperationException();
                }
                periodPenaltyList.add(periodPenalty);
            }
            problem.setPeriodPenaltyList(periodPenaltyList);
        }

//        if (Files.exists(Paths.get(parentFolder + "/合班.csv"))) {
//            //设置可用与不可用时间
//            in = new InputStreamReader(new FileInputStream(parentFolder + "/合班.csv"), "gbk");
//            records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
//            Multimap<Group, GroupEduClass> multimap = LinkedHashMultimap.create();
//            for (CSVRecord csvRecord : records) {
//                int subjectPeriodSize = Integer.parseInt(csvRecord.get("subjectPeriodSize"));
//                String groupName = csvRecord.get("groupName");
//                Group group = new Group(groupName, subjectPeriodSize);
//                GroupEduClass groupEduClass = new GroupEduClass(groupName, csvRecord.get("eduClassName"));
//                multimap.put(group, groupEduClass);
//            }
//            Map<Group, Collection<GroupEduClass>> map = multimap.asMap();
//            List<Group> groupList = new ArrayList<>(map.keySet());
//            List<GroupEduClass> groupEduClassList = map.keySet().stream().flatMap(k -> map.get(k).stream()).collect(Collectors.toList());
//            problem.setGroupList(groupList);
//            problem.setGroupEduClassList(groupEduClassList);
//        }else{
//            problem.setGroupList(new ArrayList<>());
//            problem.setGroupEduClassList(new ArrayList<>());
//        }
    }
}
