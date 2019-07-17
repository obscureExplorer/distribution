package domain;

/**
 * Created by xcy on 2019/7/16.
 */
public enum SubjectTypeEnum {
    OBLIGATORY,ACADEMIC,COLLEDGE;

    @Override
    public String toString() {
        if(this == OBLIGATORY)
            return "必修";
        else if(this == ACADEMIC)
            return "学考";
        else
            return "高考";
    }
}
