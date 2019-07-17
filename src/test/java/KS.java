import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

/**
 * Created by xcy on 2019/7/12.
 */
public class KS {
    public static void main(String[] args) {
/*        KolmogorovSmirnovTest kolmogorovSmirnovTest = new KolmogorovSmirnovTest();
        double[] d1 = {2,0,0,1,1};
        double[] d2 = {2,0,0,1,1};
        System.out.println(kolmogorovSmirnovTest.kolmogorovSmirnovTest(d1,d2));

        double[] d3 = {2,0,0,1,1};
         double[] d4 = {0,2,0,1,1};
        System.out.println(kolmogorovSmirnovTest.kolmogorovSmirnovTest(d3,d4));*/

        ChiSquareTest chiSquareTest = new ChiSquareTest();

        long[] l1 = {2,1,1,1};
        long[] l2 = {2,1,1,1};
        System.out.println(chiSquareTest.chiSquareTestDataSetsComparison(l1,l2));

    }
}
