import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.paukov.combinatorics3.Generator;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by xcy on 2019/6/2 0002.
 */
public class ClassConflictTest {
    public static void main(String[] args) throws Exception {
        // TODO: 2019/6/2 0002 读入结果csv和冲突表csv。按照period对结果排序，求出同一个period内的班级的所有组合，然后判断冲突表里是否包含了它
        InputStreamReader in;
        Iterable<CSVRecord> records;
        in = new InputStreamReader(new FileInputStream("classConflict.csv"), "gbk");
        records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        List<Pair> pairs = StreamSupport.stream(records.spliterator(), false).map(r -> {
            Pair p = new Pair(r.get("leftEduClass"), r.get("rightEduClass"));
            return p;
        }).collect(Collectors.toList());


        in = new InputStreamReader(new FileInputStream("result.csv"), "gbk");
        records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        List<Row> rows = StreamSupport.stream(records.spliterator(), false).map(r -> {
            Row row = new Row();
            row.setId(r.get("id"));
            row.setEduClass(r.get("eduClass"));
            row.setPeriod(r.get("period"));
            return row;
        }).collect(Collectors.toList());

        Map<String, List<Row>> map = rows.stream().collect(Collectors.groupingBy(Row::getPeriod));
        map.keySet().forEach(key -> {
/*            long count = map.get(key).stream().filter(item -> pairs.contains(item)).collect(Collectors.counting());
            if(count > 0){
                System.out.println(key + "存在重复");
            }*/
            //Set<Set<List<Row>>> combinations = Sets.combinations(ImmutableSet.of(map.get(key)), 2);
            Generator.combination(map.get(key)).simple(2).stream().forEach(
                    list -> {
                        Pair p = new Pair(list.get(0).getEduClass(),list.get(1).getEduClass());
                        if(pairs.contains(p)){
                            System.out.println(p);
                        }
                    }
            );

        });
    }


    public static class Row implements Comparable<Row> {
        String id, name, teacher, lectureSize, classNo, lectureIndexInCourse, eduClass, period, room;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public String getLectureSize() {
            return lectureSize;
        }

        public void setLectureSize(String lectureSize) {
            this.lectureSize = lectureSize;
        }

        public String getClassNo() {
            return classNo;
        }

        public void setClassNo(String classNo) {
            this.classNo = classNo;
        }

        public String getLectureIndexInCourse() {
            return lectureIndexInCourse;
        }

        public void setLectureIndexInCourse(String lectureIndexInCourse) {
            this.lectureIndexInCourse = lectureIndexInCourse;
        }

        public String getEduClass() {
            return eduClass;
        }

        public void setEduClass(String eduClass) {
            this.eduClass = eduClass;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        @Override
        public int compareTo(Row o) {
            return this.period.compareTo(o.period);
        }
    }

    public static class Pair {
        private String value1, value2;
        private Set<String> set;

        public Pair(String value1, String value2) {
            this.value1 = value1;
            this.value2 = value2;
            this.set = new HashSet<>();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return this.set.equals(pair);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value1, value2);
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "value1='" + value1 + '\'' +
                    ", value2='" + value2 + '\'' +
                    '}';
        }
    }
}