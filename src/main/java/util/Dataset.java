package util;

import domain.*;
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

    public static void createDataset(TimeTablingProblem problem, String timeAndRoomFilePath, String classDistributionFilePath, String resourceFilePath) throws IOException {
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

        int k = 0;
        List<RealEduClass> realEduClassList = new ArrayList<>();
        for (EduClass eduClass : eduClassList) {
            int type = eduClass.getType();
            int size;
            if(type == 0)
                size = 22;
            else if(type == 1)
                size = 4;
            else
                size = 2;
            for (int i = 0; i < size; i++) {
                RealEduClass realEduClass = new RealEduClass();
                realEduClass.setEduClass(eduClass);
                realEduClass.setIndex(i);
                realEduClassList.add(realEduClass);
                realEduClass.setId(++k);
            }

        }
        problem.setRealEduClassList(realEduClassList);
    }
}
