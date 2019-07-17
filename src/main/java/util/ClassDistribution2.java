package util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import domain.EduClass;
import domain.EduClassTypeEnum;
import domain.Student;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.javatuples.Quartet;
import org.javatuples.Septet;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by xcy on 2019/5/29.
 */
public class ClassDistribution2 {

    public static void main(String[] args) throws IOException {
        InputStreamReader in = new InputStreamReader(new FileInputStream("英才学生选科数据.csv"), "gbk");
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

        Set<String> allSubjects = Sets.newHashSet("物理", "化学", "生物", "历史", "政治", "地理");

        List<Quartet<String, String, String, String>> quartets = StreamSupport.stream(records.spliterator(), false)
                .map(t -> new Quartet<String, String, String, String>(t.get("name"), t.get("subject1"), t.get("subject2"), t.get("subject3")))
                .collect(Collectors.toList());

        List<Septet<String, String, String, String, String, String, String>> septets = quartets.stream().map(quartet -> {
            Set<String> set2 = Sets.newHashSet(quartet.getValue1(), quartet.getValue2(), quartet.getValue3());
            Sets.SetView<String> difference = Sets.difference(allSubjects, set2);

            Iterator<String> iterator = difference.iterator();
            Septet<String, String, String, String, String, String, String> septet = new Septet(quartet.getValue0(),
                    quartet.getValue1(),
                    quartet.getValue2(),
                    quartet.getValue3(),
                    "会考" + iterator.next(),
                    "会考" + iterator.next(),
                    "会考" + iterator.next()
            );
            return septet;
        }).collect(Collectors.toList());


/*        Map<String, List<Student>> maps = new LinkedHashMap<>();
        maps.put("物理", new ArrayList<>());
        maps.put("化学", new ArrayList<>());
        maps.put("生物", new ArrayList<>());
        maps.put("历史", new ArrayList<>());
        maps.put("政治", new ArrayList<>());
        maps.put("地理", new ArrayList<>());
        maps.put("会考物理", new ArrayList<>());
        maps.put("会考化学", new ArrayList<>());
        maps.put("会考生物", new ArrayList<>());
        maps.put("会考历史", new ArrayList<>());
        maps.put("会考政治", new ArrayList<>());
        maps.put("会考地理", new ArrayList<>());
        septets.forEach(septet -> {
            Student student = new Student();
            student.setName(septet.getValue0());
            maps.get(septet.getValue1()).add(student);
            maps.get(septet.getValue2()).add(student);
            maps.get(septet.getValue3()).add(student);
            maps.get(septet.getValue4()).add(student);
            maps.get(septet.getValue5()).add(student);
            maps.get(septet.getValue6()).add(student);
        });

        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("英才选科数据（带学考）.csv"), Charset.forName("gbk"));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("name", "subject1", "subject2", "subject3", "subject4", "subject5", "subject6"))
        ) {
            for (Septet q : septets) {
                csvPrinter.printRecord(q.getValue0(), q.getValue1(), q.getValue2(),
                        q.getValue3(), q.getValue4(), q.getValue5(), q.getValue6());
            }
            csvPrinter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Table<String, String, String> table = HashBasedTable.create();
        septets.forEach(septet -> {
            table.put(septet.getValue0(), septet.getValue1(), septet.getValue0());
            table.put(septet.getValue0(), septet.getValue2(), septet.getValue0());
            table.put(septet.getValue0(), septet.getValue3(), septet.getValue0());
            table.put(septet.getValue0(), septet.getValue4(), septet.getValue0());
            table.put(septet.getValue0(), septet.getValue5(), septet.getValue0());
            table.put(septet.getValue0(), septet.getValue6(), septet.getValue0());
        });

        in = new InputStreamReader(new FileInputStream("dataset/5/分班数据old.csv"), "gbk");
        records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

        ListMultimap<EduClass, Student> eduClassMultiMap = MultimapBuilder.treeKeys().arrayListValues().build();
        for (CSVRecord record : records) {
            String typeName = record.get("type");
            if (!typeName.equals("行政班"))
                continue;
            EduClassTypeEnum type;
            switch (typeName) {
                case "行政班":
                    type = EduClassTypeEnum.ADMINISTRATIVE;
                    break;
                case "会考班":
                    type = EduClassTypeEnum.ACADEMIC;
                    break;
                default:
                    type = EduClassTypeEnum.COLLEGE;
                    break;
            }
            String studentName = record.get("studentName");
            Student student = new Student();
            student.setName(studentName);
            EduClass eduClass = new EduClass();
            eduClass.setName(record.get("className"));
            eduClass.setType(type);
            eduClassMultiMap.put(eduClass, student);
        }

        Map<EduClass, Collection<Student>> eduClassCollectionMap = eduClassMultiMap.asMap();
        List<EduClass> eduClassList = new ArrayList<>();
        for (EduClass key : eduClassCollectionMap.keySet()) {
            key.setStudents((List<Student>) eduClassCollectionMap.get(key));
            eduClassList.add(key);
        }


        Map<String, Integer> subjectIndex = new LinkedHashMap<>();

        eduClassCollectionMap.keySet().forEach(eduClass -> {
            ListMultimap<String, Student> treeListMultimap = MultimapBuilder.treeKeys().arrayListValues().build();
            eduClassCollectionMap.get(eduClass).forEach(currentStudent -> {
                //key是这个学生的选考科目和学考科目，value是学生的名字
                Map<String, String> row = table.row(currentStudent.getName());
                row.keySet().forEach(subjectName -> {
                    treeListMultimap.put(subjectName, currentStudent);
                });
            });
            Map<String, Collection<Student>> map = treeListMultimap.asMap();


            map.keySet().forEach(k -> {
                subjectIndex.putIfAbsent(k, 1);
                EduClass currentEduClass = new EduClass();
                currentEduClass.setName(k + "班" + subjectIndex.get(k));
                subjectIndex.put(k, subjectIndex.get(k) + 1);
                String subjectName;
                if (k.startsWith("会考")){
                    currentEduClass.setType(EduClassTypeEnum.ACADEMIC);
                    subjectName = k.replace("会考","");
                }
                else{
                    currentEduClass.setType(EduClassTypeEnum.COLLEGE);
                    subjectName = k;
                }
                currentEduClass.setSubjectName(subjectName);
                currentEduClass.setStudents((List<Student>) map.get(k));
                eduClassList.add(currentEduClass);
            });
        });


        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("dataset/5/新的分班数据.csv"), Charset.forName("gbk"));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("className", "studentName", "subjectName", "type"))
        ) {
            eduClassList.forEach(eduClass -> {
                eduClass.getStudents().forEach(student -> {
                    try {
                        String subjectName = eduClass.getSubjectName();
                        csvPrinter.printRecord(eduClass.getName(),student, subjectName == null ? "行政班": subjectName ,eduClass.getType());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
            csvPrinter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EduClass> classes = eduClassList.stream().filter(e -> e.getType() == EduClassTypeEnum.ADMINISTRATIVE).collect(Collectors.toList());
        List<EduClass> classes2 = eduClassList.stream().filter(e -> e.getType() != EduClassTypeEnum.ADMINISTRATIVE).collect(Collectors.toList());
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
