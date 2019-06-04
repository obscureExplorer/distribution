import domain.Course;
import domain.EduClass;
import domain.Lecture;
import domain.Period;
import domain.Room;
import domain.TimeTablingProblem;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import util.Dataset;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by xcy on 2019/5/15.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // 构建Solver
        SolverFactory<TimeTablingProblem> solverFactory = SolverFactory.createFromXmlResource("config/solverConfig.xml");
        Solver<TimeTablingProblem> solver = solverFactory.buildSolver();
        // 初始化数据
        TimeTablingProblem problem = new TimeTablingProblem();
        Dataset.createDataset(problem,"时间地点2.csv","分班数据2.csv","教学资源2.csv");
        // 开始排课
        TimeTablingProblem solvedProblem = solver.solve(problem);
        System.out.println(solvedProblem.getScore());
        System.out.println(solver.explainBestScore());
        // 写入结果
        // 如果将以下的这些字段作为数据库表字段，那么这张表不满足2nf
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String time = formatter.format(LocalDateTime.now());
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("result_" + time + ".csv"), Charset.forName("gbk"));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("id", "name", "teacher", "type", "lectureSize", "classNo", "lectureIndexInCourse", "eduClass","eduClassType", "period", "room"))
        ) {
            List<Lecture> solvedLectures = solvedProblem.getLectureList();
            for (Lecture solvedLecture : solvedLectures) {
                Course course = solvedLecture.getCourse();
                EduClass eduClass = solvedLecture.getEduClass();
                Period period = solvedLecture.getPeriod();
                Room room = solvedLecture.getRoom();
                csvPrinter.printRecord(solvedLecture.getId(), course.getName(), course.getTeacher(), course.getType(),
                        course.getLectureSize(), course.getClassNo(), solvedLecture.getLectureIndexInCourse(),
                        eduClass.toString(),eduClass.getType(), period, room);
            }
            csvPrinter.flush();
        }
/*
        //基准测试
        PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromSolverFactory(solverFactory);
        PlannerBenchmark plannerBenchmark = benchmarkFactory.buildPlannerBenchmark(problem);
        plannerBenchmark.benchmarkAndShowReportInBrowser();
*/

    }

}
