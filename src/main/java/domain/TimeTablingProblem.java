package domain;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xcy on 2019/5/15.
 */
@PlanningSolution
public class TimeTablingProblem implements Serializable {

    private static final long serialVersionUID = 2869218948801133466L;

    @ProblemFactCollectionProperty
    private List<Day> dayList;

    @ProblemFactCollectionProperty
    private List<Timeslot> timeslotList;

    @ValueRangeProvider(id = "periodRange")
    @ProblemFactCollectionProperty
    private List<Period> periodList;

    @ValueRangeProvider(id = "roomRange")
    @ProblemFactCollectionProperty
    private List<Room> roomList;

    @ProblemFactCollectionProperty
    private List<EduClass> eduClassList;

    @PlanningEntityCollectionProperty
    private List<RealEduClass> realEduClassList;

    @PlanningScore
    private HardSoftScore score;

    public List<Day> getDayList() {
        return dayList;
    }

    public void setDayList(List<Day> dayList) {
        this.dayList = dayList;
    }

    public List<Timeslot> getTimeslotList() {
        return timeslotList;
    }

    public void setTimeslotList(List<Timeslot> timeslotList) {
        this.timeslotList = timeslotList;
    }

    public List<Period> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<Period> periodList) {
        this.periodList = periodList;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    public List<EduClass> getEduClassList() {
        return eduClassList;
    }

    public void setEduClassList(List<EduClass> eduClassList) {
        this.eduClassList = eduClassList;
    }

    @ProblemFactCollectionProperty
    private List<EduClassConflict> calculateClassConflict() {
        List<EduClassConflict> eduClassConflictList = new ArrayList<>();
        int size = eduClassList.size();
        //班级与班级之间的冲突
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                EduClass leftEduClass = eduClassList.get(i);
                EduClass rightEduClass = eduClassList.get(j);

                List<Student> leftStudents = leftEduClass.getStudents();
                List<Student> rightStudents = rightEduClass.getStudents();
                long conflictCount = leftStudents.stream()
                        .filter(rightStudents::contains)
                        .collect(Collectors.counting());
                if (conflictCount > 0)
                   eduClassConflictList.add(new EduClassConflict(leftEduClass, rightEduClass, (int) conflictCount));
                    //eduClassConflictList.add(new EduClassConflict(leftEduClass, rightEduClass, 1));
            }
        }
/*        //与自身构成冲突
        for (EduClass eduClass : eduClassList) {
             eduClassConflictList.add(new EduClassConflict(eduClass, eduClass, eduClass.getStudents().size()));
        }*/

        if(!Files.exists(Paths.get("classConflict.csv"))){
            try (
                    BufferedWriter writer = Files.newBufferedWriter(Paths.get("classConflict.csv"), Charset.forName("gbk"));
                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("leftEduClass","rightEduClass"))
            ) {
                eduClassConflictList.forEach(e -> {
                    try {
                        csvPrinter.printRecord(e.getLeftEduClass(),e.getRightEduClass());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                });
                csvPrinter.flush();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return eduClassConflictList;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<RealEduClass> getRealEduClassList() {
        return realEduClassList;
    }

    public void setRealEduClassList(List<RealEduClass> realEduClassList) {
        this.realEduClassList = realEduClassList;
    }
}
