package domain;

/**
 * 合并的班级
 * Created by xcy on 2019/7/18.
 */
public class GroupEduClass {
    private String groupName;
    private String eduClassName;

    public GroupEduClass(String groupName, String eduClassName) {
        this.groupName = groupName;
        this.eduClassName = eduClassName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEduClassName() {
        return eduClassName;
    }

    public void setEduClassName(String eduClassName) {
        this.eduClassName = eduClassName;
    }
}
