package domain;

import org.optaplanner.core.api.domain.lookup.PlanningId;

/**
 * Created by xcy on 2019/7/11.
 */
public class PeriodPenalty {
    @PlanningId
    private Long id;
    private String name;
    private String subjectName;
    private Period period;
    //表示name是eduClass还是teacher
    private ResourceTypeEnum resourceType;
    private RuleTypeEnum ruleType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public ResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public RuleTypeEnum getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleTypeEnum ruleType) {
        this.ruleType = ruleType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
