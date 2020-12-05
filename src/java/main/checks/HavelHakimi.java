package checks;

import java.util.*;

public class HavelHakimi {
    int[] dd = {4,4,3,3,2,2,1,1};
    List<Integer> d = new ArrayList<>();

    boolean check() {
        while (true) {
            d.sort(Collections.reverseOrder());
            if (d.get(0) == 0) {
                return true;
            }

            int v = d.remove(0);
            if (v > d.size()) {
                return false;
            }
            for (int i = 0; i < v; i++) {
                d.set(i, d.get(i) - 1);
            }
        }
    }

    void solveAndPrint() {
        for (int i = 0, n = dd.length; i < n; i++) {
            d.add(dd[i]);
        }
        System.out.println(check() ? "YES" : "NO");
    }

    public static void main(String[] args) {
        new HavelHakimi().solveAndPrint();
    }
}
