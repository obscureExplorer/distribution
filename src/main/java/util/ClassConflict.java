package util;

import domain.EduClass;
import domain.Student;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by xcy on 2019/6/25.
 */
public class ClassConflict {
    public static void main(String[] args) throws IOException {
        //设置分班数据
        InputStreamReader in = new InputStreamReader(new FileInputStream("dataset/5/分班数据.csv"), "gbk");
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        Map<EduClass, List<Student>> classStudentMap = new LinkedHashMap<>();

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
        eduClassList.sort(Comparator.comparing(EduClass::getType));


        List<EduClass> classes = eduClassList.stream().filter(e -> e.getType() == 0).collect(Collectors.toList());
        List<EduClass> classes2 = eduClassList.stream().filter(e -> e.getType() != 0).collect(Collectors.toList());
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("教学班对应的行政班.csv"), Charset.forName("gbk"));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("className", "className2"))
        ) {
            classes2.forEach(c2 ->{
                Set<String> sets = new HashSet<>();
                for (Student student : c2.getStudents()) {
                    for (EduClass c1 : classes) {
                        if(c1.getStudents().contains(student)){
                            sets.add(c1.getName());
                            break;
                        }
                    }
                }
                try {
                    csvPrinter.printRecord(c2.getName(),String.join(",", sets));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            csvPrinter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
