package util;

import domain.EduClass;
import domain.Student;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by xcy on 2019/5/29.
 */
public class ClassDistribution {

    public static void main(String[] args) throws IOException {
        InputStreamReader in = new InputStreamReader(new FileInputStream("英才学生选科数据.csv"), "gbk");
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        Map<String, List<String>> map = new LinkedHashMap<>();
        map.put("物理", new ArrayList<>());
        map.put("化学", new ArrayList<>());
        map.put("生物", new ArrayList<>());
        map.put("历史", new ArrayList<>());
        map.put("政治", new ArrayList<>());
        map.put("地理", new ArrayList<>());
        //将学生放到对应的课程里面去
        for (CSVRecord record : records) {
            String name = record.get("name");
            String subject1 = record.get("subject1");
            String subject2 = record.get("subject2");
            String subject3 = record.get("subject3");
            map.get(subject1).add(name);
            map.get(subject2).add(name);
            map.get(subject3).add(name);

            String administrativeClass = record.get("administrativeClass");
            if(map.containsKey(administrativeClass)){
                map.get(administrativeClass).add(name);
            }else {
                map.put(administrativeClass,new ArrayList<>());
            }
        }

        int start = 0;
        List<EduClass> eduClasses = new ArrayList<>();
        map.keySet().forEach(key -> {
            List<String> students = map.get(key);
            List<Integer> classSizes = dispatchStudentsWithDP(students.size(), 35, 46);
            classSizes.forEach(size ->{
                EduClass eduClass = new EduClass();
                if(key.contains("行政"))
                    eduClass.setType(0);
                else
                    eduClass.setType(1);
                eduClass.setName(key);
                List<Student> classStudents = IntStream.range(start, size).mapToObj(i -> {
                    Student s = new Student();
                    s.setName(students.get(i));
                    return s;
                }).collect(Collectors.toList());
                eduClass.setStudents(classStudents);
                eduClasses.add(eduClass);
            });
        });

        System.out.println(1);
    }


    //用动态规划的方法，将integerNum分解成x1+x2+..xn之和，其中 lowerBound <= x1...xn <= upperBound，使得n最小
    //自底向上的制表法
    public static List<Integer> dispatchStudentsWithDP(int integerNum, int lowerBound, int upperBound) {
        //存放可划分的个数
        int[] arr = new int[integerNum + 1];
        int[] arr2 = new int[integerNum + 1];
        //前面小于lowerBound的部分全都是0，代表全部不可分解
        for (int i = 0; i < integerNum && i < lowerBound; i++) {
            arr[i] = 0;
        }
        for (int i = lowerBound; i < integerNum + 1; i++) {
            int min = Integer.MAX_VALUE;
            boolean integerNumInRange = false;
            int currentNum = 0;
            for (int j = lowerBound; j <= upperBound; ++j) {
                int remaining = i - j;
                //这里说明 lowerBound <= integerNum <= upperBound
                if (remaining == 0) {
                    integerNumInRange = true;
                    break;
                }
                int tmp = arr[remaining];
                if (tmp > 0 && tmp < min) {
                    min = tmp;
                    currentNum = j;
                }
            }
            //lowerBound <= integerNum <= upperBound的特殊情况
            if (integerNumInRange) {
                arr2[i] = i;
                arr[i] = 1;
            } else if (min != Integer.MAX_VALUE) {
                arr[i] = 1 + min;
                arr2[i] = currentNum;
            } else {
                //该integerNum无法满足设置的条件，则表格中该integerNum对应的位置填0，表示不可分解。
                arr[i] = 0;
            }
        }
        List<Integer> result = new ArrayList<>();
        int t = integerNum;
        while (t > 0) {
            result.add(arr2[t]);
            t -= arr2[t];
        }
        return result;
    }


    //用动态规划的方法，将integerNum分解成x1+x2+..xn之和，其中 lowerBound <= x1...xn <= upperBound，使得n最小
    //自底向上的制表法
    public static int dp(int integerNum, int lowerBound, int upperBound) {
        //存放可划分的个数
        int[] arr = new int[integerNum + 1];
        int[] arr2 = new int[integerNum + 1];
        //前面小于lowerBound的部分全都是0，代表全部不可分解
        for (int i = 0; i < integerNum && i < lowerBound; i++) {
            arr[i] = 0;
        }
        for (int i = lowerBound; i < integerNum + 1; i++) {
            int min = Integer.MAX_VALUE;
            boolean integerNumInRange = false;
            int currentNum = 0;
            for (int j = lowerBound; j <= upperBound; ++j) {
                int remaining = i - j;
                //这里说明 lowerBound <= integerNum <= upperBound
                if (remaining == 0) {
                    integerNumInRange = true;
                    break;
                }
                int tmp = arr[remaining];
                if (tmp > 0 && tmp < min) {
                    min = tmp;
                    currentNum = j;
                }
            }
            //lowerBound <= integerNum <= upperBound的特殊情况
            if (integerNumInRange) {
                arr2[i] = i;
                arr[i] = 1;
            } else if (min != Integer.MAX_VALUE) {
                arr[i] = 1 + min;
                arr2[i] = currentNum;
            } else {
                //该integerNum无法满足设置的条件，则表格中该integerNum对应的位置填0，表示不可分解。
                arr[i] = 0;
            }
        }
        int t = integerNum;
        while (t > 0) {
            System.out.print("===" + arr2[t] + "==\n");
            t -= arr2[t];
        }
        return arr[integerNum];
    }


}
