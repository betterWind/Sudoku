package sudoku.lite;

import java.math.BigInteger;
import java.util.List;

public class SudokuUtil {
    /**
     * Clears the list. The steps are not nullfied, but the list items are.
     *
     * @param steps
     */
    public static void clearStepList(List<SolutionStep> steps) {
        if (steps != null) {
            for (int i = 0; i < steps.size(); i++) {
                steps.set(i, null);
            }
            steps.clear();
        }
    }

    /**
     * Calculates n over k
     *
     * @param n
     * @param k
     * @return
     */
    public static int combinations(int n, int k) {
        if (n <= 167) {
            double fakN = 1;
            for (int i = 2; i <= n; i++) {
                fakN *= i;
            }
            double fakNMinusK = 1;
            for (int i = 2; i <= n - k; i++) {
                fakNMinusK *= i;
            }
            double fakK = 1;
            for (int i = 2; i <= k; i++) {
                fakK *= i;
            }
            return (int) (fakN / (fakNMinusK * fakK));
        } else {
            BigInteger fakN = BigInteger.ONE;
            for (int i = 2; i <= n; i++) {
                fakN = fakN.multiply(new BigInteger(i + ""));
            }
            BigInteger fakNMinusK = BigInteger.ONE;
            for (int i = 2; i <= n - k; i++) {
                fakNMinusK = fakNMinusK.multiply(new BigInteger(i + ""));
            }
            BigInteger fakK = BigInteger.ONE;
            for (int i = 2; i <= k; i++) {
                fakK = fakK.multiply(new BigInteger(i + ""));
            }
            fakNMinusK = fakNMinusK.multiply(fakK);
            fakN = fakN.divide(fakNMinusK);
            return fakN.intValue();
        }
    }
}
