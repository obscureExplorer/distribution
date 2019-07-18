package domain;

/**
 * Created by xcy on 2019/7/16.
 */
public enum EduClassTypeEnum {
    ADMINISTRATIVE,ACADEMIC, COLLEGE, GROUP;


    @Override
    public String toString() {
        switch (this){
            case ADMINISTRATIVE:
                return "行政班";
            case ACADEMIC:
                return "会考班";
            case COLLEGE:
                return "高考班";
            case GROUP:
                return "合班";
            default:
                return "未知";
        }
    }
}
