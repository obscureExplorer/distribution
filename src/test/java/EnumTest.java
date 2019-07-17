import domain.SubjectTypeEnum;
import org.junit.Test;

/**
 * Created by xcy on 2019/7/16.
 */
public class EnumTest {
    @Test
    public void name() {
        SubjectTypeEnum s = SubjectTypeEnum.ACADEMIC;
        System.out.println(s);
    }
}
