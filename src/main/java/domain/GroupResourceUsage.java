package domain;

/**
 * Created by xcy on 2019/7/18.
 */
public class GroupResourceUsage {
    private String groupName;
    private GroupResourceTypeEnum groupResourceType;
    private Object resource;

    public GroupResourceUsage(String groupName, GroupResourceTypeEnum groupResourceType, Object resource) {
        this.groupName = groupName;
        this.groupResourceType = groupResourceType;
        this.resource = resource;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public GroupResourceTypeEnum getGroupResourceType() {
        return groupResourceType;
    }

    public void setGroupResourceType(GroupResourceTypeEnum groupResourceType) {
        this.groupResourceType = groupResourceType;
    }

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }
}
