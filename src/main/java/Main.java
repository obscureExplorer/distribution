import domain.RealEduClass;
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
        Dataset.createDataset(problem,"时间地点2.csv","分班数据2.csv","教学资源3.csv");
        // 开始排课
        TimeTablingProblem solvedProblem = solver.solve(problem);
        System.out.println(solvedProblem.getScore());
        System.out.println(solver.explainBestScore());
        // 写入结果
        // 如果将以下的这些字段作为数据库表字段，那么这张表不满足2nf
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String time = formatter.format(LocalDateTime.now());
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("class_result_" + time + ".csv"), Charset.forName("gbk"));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("id", "eduClass","eduClassIndex", "period", "room"))
        ) {
            List<RealEduClass> solvedEduClassList = solvedProblem.getRealEduClassList();
            for (RealEduClass realEduClass : solvedEduClassList) {
                csvPrinter.printRecord(realEduClass.getId(),realEduClass.getEduClass(),realEduClass.getIndex(),realEduClass.getPeriod(),realEduClass.getRoom());
            }
            csvPrinter.flush();
        }

       /* ScoreDirectorFactory<TimeTablingProblem> scoreDirectorFactory = solver.getScoreDirectorFactory();
        try (ScoreDirector<TimeTablingProblem> guiScoreDirector = scoreDirectorFactory.buildScoreDirector()) {
            Collection<ConstraintMatchTotal> constraintMatchTotals = guiScoreDirector.getConstraintMatchTotals();
            for (ConstraintMatchTotal constraintMatchTotal : constraintMatchTotals) {
                String constraintName = constraintMatchTotal.getConstraintName();
                // The score impact of that constraint
                HardSoftScore totalScore = (HardSoftScore) constraintMatchTotal.getScore();
                for (ConstraintMatch constraintMatch : constraintMatchTotal.getConstraintMatchSet()) {
                    List<Object> justificationList = constraintMatch.getJustificationList();
                    HardSoftScore score = (HardSoftScore) constraintMatch.getScore();
                    System.out.println(score.toShortString() + "[" + "]");
                }
            }
        }*/
/*
        //基准测试
        PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromSolverFactory(solverFactory);
        PlannerBenchmark plannerBenchmark = benchmarkFactory.buildPlannerBenchmark(problem);
        plannerBenchmark.benchmarkAndShowReportInBrowser();
*/

    }

}
