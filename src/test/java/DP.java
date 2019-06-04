import org.junit.Assert;
import org.junit.Test;
import util.ClassDistribution;

/**
 * Created by xcy on 2019/5/29.
 */
public class DP {

    @Test
    public void integerNumLessThanLowerBound() {
        Assert.assertEquals(0, ClassDistribution.dp(30, 35, 45));
    }

    @Test
    public void integerNumInRange() {
        Assert.assertEquals(1, ClassDistribution.dp(37, 35, 45));
    }

    @Test
    public void integerNumEqualsLowerBound(){
        Assert.assertEquals(1, ClassDistribution.dp(35, 35, 45));
    }

    @Test
    public void integerNumEqualsUpperBound(){
        Assert.assertEquals(1, ClassDistribution.dp(45, 35, 45));
    }

    @Test
    public void LowerBoundGraterThanUpperBoundAndLessThanDoubleOfLowerBound(){
        Assert.assertEquals(0, ClassDistribution.dp(69, 35, 45));
    }

    @Test
    public void test4(){
        Assert.assertEquals(2, ClassDistribution.dp(90, 35, 45));
    }

    @Test
    public void test5(){
        Assert.assertEquals(3, ClassDistribution.dp(105, 35, 45));
    }

    @Test
    public void test6(){
        System.out.println(ClassDistribution.dp(44, 35, 46));
        System.out.println(ClassDistribution.dp(118, 35, 46));
        System.out.println(ClassDistribution.dp(151, 35, 46));
        System.out.println(ClassDistribution.dp(79, 35, 46));
        System.out.println(ClassDistribution.dp(108, 35, 46));
        System.out.println(ClassDistribution.dp(91, 35, 46));
    }

}
