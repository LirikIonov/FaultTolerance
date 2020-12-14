package checks;

import graph.DrawGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class HavelHakimi {
    static final String inputFile = "input.txt";
    static final String outputFile = "output.txt";

    static final Set<Character> notDigits = Set.of(',', '(', ')', ' ', '.', '{', '}');

    void printVec(List<Integer> d, int lineNumber) {
        StringBuilder res = new StringBuilder();
        res.append(lineNumber + ": ");
        for (int i = 0; i < d.size(); i++) {
            res.append(d.get(i)).append(" ");
        }
        System.out.println(res);
    }

    boolean check(List<Integer> d) {
        List<Integer> vec = new ArrayList<>(d);
        int lineNumber = 0;
        while (true) {
            vec.sort(Collections.reverseOrder());
            printVec(vec, ++lineNumber);

            if (vec.get(0) == 0) {
                System.out.println("Вектор графичен\n");
                return true;
            }

            int v = vec.remove(0);
            if (v > vec.size()) {
                System.out.println("Вектор не является графичным\n");
                return false;
            }
            for (int i = 0; i < v; i++) {
                vec.set(i, vec.get(i) - 1);
                if (vec.get(i) < 0) {
                    System.out.println("Вектор не является графичным\n");
                    return false;
                }
            }
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
        new HavelHakimi().solveAndPrint();
    }
}
