package checks;

import java.util.Arrays;
import java.util.Collections;

public class ErdosGallai {
    int[] d = {4,4,3,3,2,2,1,1};

    boolean check() {
        d = Arrays.stream(d)
                .boxed()
                .sorted(Collections.reverseOrder())
                .mapToInt(Integer::intValue)
                .toArray();
        long dSum = 0;

        for (int k = 0, n = d.length; k < n; k++) {
            long kMult = (long) k * (k - 1);
            long kSum = 0;
            for (int j = k + 1; j < n; j++) {
                kSum += Math.min(j, d[j]);
            }

            dSum += d[k];
            if (dSum > kMult + kSum) {
                return false;
            }
        }

        return dSum % 2 == 0;
    }

    void solveAndPrint() {
        System.out.println(check() ? "YES" : "NO");
    }

    public static void main(String[] args) {
        new ErdosGallai().solveAndPrint();
    }
}
