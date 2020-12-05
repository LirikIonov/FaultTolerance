package checks;

import java.io.FileNotFoundException;

public class PruferCodeToTree {
    String pruferStr = "141377";

    public void solve() {
        int len = pruferStr.length();
        int[] prufer = new int[len];
        for (int i = 0, n = len; i < n; i++) {
            prufer[i] = pruferStr.charAt(i) - '0';
        }

        int n = prufer.length + 2;
        int[] v = new int[n];
        for (int i = 0; i < n - 2; i++) {
            v[prufer[i] - 1]++;
        }

        int j;
        for (int i = 0; i < n - 2; i++) {
            for (j = 0; j < n; j++) {
                if (v[j] == 0) {
                    v[j] = -1;
                    System.out.println((j + 1) + " " + prufer[i]);
                    v[prufer[i] - 1]--;
                    break;
                }
            }
        }


        for (int i = 0; i < n; i++) {
            if (v[i] == 0) {
                System.out.print(i + 1 + " ");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new PruferCodeToTree().solve();
    }
}
