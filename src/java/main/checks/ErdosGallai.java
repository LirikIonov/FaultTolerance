package checks;

import graph.DrawGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class ErdosGallai {
    static final String inputFile = "input.txt";
    static final String outputFile = "output.txt";

    static final Set<Character> notDigits = Set.of(',', '(', ')', ' ', '.', '{', '}');

    boolean check(List<Integer> d) {
        d.sort(Collections.reverseOrder());
        System.out.println(d);
        long dSum = 0;

        for (int k = 1, n = d.size(); k <= n; k++) {
            long kMult = (long) k * (k - 1);
            long kSum = 0;
            for (int j = k; j < n; j++) {
                kSum += Math.min(k, d.get(j));
            }

            dSum += d.get(k - 1);
            if (dSum > kMult + kSum) {
                System.out.println(k + ": " + dSum + " > " + kMult + " + " + kSum);
                System.out.println("Вектор не является графичным\n");
                return false;
            }
            System.out.println(k + ": " + dSum + " <= " + kMult + " + " + kSum);
        }

        if (dSum % 2 == 0) {
            System.out.println("Вектор графичен\n");
            return true;
        }
        else {
            System.out.println("Вектор не является графичным\n");
            return false;
        }
    }

    void solveAndPrint() throws FileNotFoundException {
        Scanner in = new Scanner(new File(inputFile));
        PrintWriter out = new PrintWriter(new FileOutputStream(outputFile), true);
        int times = 1;

        while (in.hasNextLine()) {
            String vec = in.nextLine();
            if (vec.isEmpty()) {
                continue;
            }

            List<Integer> d = new ArrayList<>();
            int start = 0;
            if ((vec.contains(".") && vec.indexOf(".") < vec.length() - 1) || vec.contains("(")) {
                start = Math.max(vec.indexOf('.'), vec.lastIndexOf("(")) + 1;
            }

            for (int len = vec.length(); start < len; start++) {
                char c = vec.charAt(start);
                if (!notDigits.contains(c)) {
                    int num = Character.getNumericValue(c);
                    d.add(num);
                }
            }

            boolean res = check(d);
            String verdict = " — " + (res ? "графичный. " : "не является графичным. ");
            out.print(times++ + ". " + vectorToStr(d) + verdict);
            if (res) {
                printMaybeHamiltonianpath(d, out);
            }
            out.println();
        }
        out.close();
    }

    private void printMaybeHamiltonianpath(List<Integer> d, PrintWriter out) {
        int n = d.size();
        DrawGraph graph = new DrawGraph(n, n - 1);
        graph.buildGraphFromSequence(d, out);
        graph.drawWithDegrees();
    }

    private String vectorToStr(List<Integer> vec) {
        return vec.toString().replace("[", "(").replace("]", ")");
    }

    public static void main(String[] args) throws FileNotFoundException {
        new ErdosGallai().solveAndPrint();
    }
}
