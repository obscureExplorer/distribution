import domain.EduClass;
import domain.LectureOfEduClass;
import domain.Period;
import domain.Room;
import domain.Subject;
import domain.Teacher;
import domain.TimeTablingProblem;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.constraint.ConstraintMatch;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirectorFactory;
import util.Dataset;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
        Dataset.createDataset(problem, "dataset/5");
        //添加listener，用来监听分值发生变化的事件
        solver.addEventListener(event -> {
            // 忽略不可行的解
            if (event.getNewBestSolution().getScore().isFeasible()) {
                System.out.println(event.getNewBestScore().toString());
            }
        });

        // 开始排课
        TimeTablingProblem solvedProblem = solver.solve(problem);

        // 写入结果
        // 如果将以下的这些字段作为数据库表字段，那么这张表不满足2nf
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String time = formatter.format(LocalDateTime.now());
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("result_" + time + ".csv"), Charset.forName("gbk"));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("id", "name", "teacher", "type", "lectureIndex", "eduClass", "eduClassType", "period", "room"))
        ) {
            List<LectureOfEduClass> solvedLectures = solvedProblem.getLectureList();
            for (LectureOfEduClass solvedLecture : solvedLectures) {
                EduClass eduClass = solvedLecture.getEduClass();
                Period period = solvedLecture.getPeriod();
                Room room = solvedLecture.getRoom();
                Subject subject = solvedLecture.getSubject();
                Teacher teacher = solvedLecture.getTeacher();
                csvPrinter.printRecord(solvedLecture.getId(), subject.getName(), teacher.getName(), subject.getType()
                        ,solvedLecture.getLectureIndex(),
                        eduClass, eduClass.getType(), period, room);
            }
            csvPrinter.flush();
        }

        ScoreDirectorFactory<TimeTablingProblem> scoreDirectorFactory = solver.getScoreDirectorFactory();
        try (ScoreDirector<TimeTablingProblem> guiScoreDirector = scoreDirectorFactory.buildScoreDirector()) {
            guiScoreDirector.setWorkingSolution(solvedProblem);
            //记录了被打破的规则
            Collection<ConstraintMatchTotal> constraintMatchTotals = guiScoreDirector.getConstraintMatchTotals();
            for (ConstraintMatchTotal constraintMatchTotal : constraintMatchTotals) {
                String constraintName = constraintMatchTotal.getConstraintName();
                // 这条被打破的规则对分值的影响
                Score totalScore =  constraintMatchTotal.getScore();
                // 哪些对象打破了这条规则
                Set<ConstraintMatch> constraintMatchSet = constraintMatchTotal.getConstraintMatchSet();
                System.out.println(totalScore.toShortString() + " constraint(" + constraintName + ") has " + constraintMatchSet.size() + " matches");
                for (ConstraintMatch constraintMatch : constraintMatchSet) {
                    List<Object> justificationList = constraintMatch.getJustificationList();
                    Score score = constraintMatch.getScore();
                    System.out.println(score.toShortString() + justificationList.toString());
                }
            }
        }
/*        //基准测试
        PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromSolverFactory(solverFactory);
        PlannerBenchmark plannerBenchmark = benchmarkFactory.buildPlannerBenchmark(problem);
        plannerBenchmark.benchmarkAndShowReportInBrowser();*/

    }

}
