package checks;

import graph.DrawGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class PruferCodeToTree {
    static final String inputFile = "input.txt";
    static final String outputFile = "output.txt";

    static final Set<Character> notDigits = Set.of(',', '(', ')', ' ', '.', '{', '}');

    public void solve() throws FileNotFoundException {
        Scanner in = new Scanner(new File(inputFile));
        String pruferStr = in.nextLine();
        List<Integer> prufer = new ArrayList<>();
        int start = 0;

        for (int len = pruferStr.length(); start < len; ) {
            char c = pruferStr.charAt(start);
            if (notDigits.contains(c)) {
                ++start;
                continue;
            }

            String str = "";
            while (!notDigits.contains(c) && start++ < len) {
                str += c;
                if (start < len) {
                    c = pruferStr.charAt(start);
                }
            }
            prufer.add(Integer.parseInt(str));
        }

        int n = prufer.size() + 2;
        int[] v = new int[n];
        for (int i = 0; i < n - 2; i++) {
            v[prufer.get(i) - 1]++;
        }

        PrintWriter out = new PrintWriter(new FileOutputStream(outputFile), true);
        DrawGraph graph = new DrawGraph(n, n - 1);
        out.println(n + " " + (n - 1));
        int j;
        for (int i = 0; i < n - 2; i++) {
            for (j = 0; j < n; j++) {
                if (v[j] == 0) {
                    v[j] = -1;
                    out.println((j + 1) + " " + prufer.get(i));
                    graph.addEdge(j, prufer.get(i) - 1);
                    v[prufer.get(i) - 1]--;
                    break;
                }
            }
        }

        int a = -1, b = -1;
        for (int i = 0; i < n; i++) {
            if (v[i] == 0) {
                a = b;
                b = i;
                out.print(i + 1 + " ");
            }
        }
        out.print("\n\n");
        graph.addEdge(a, b);
        graph.draw();

        printEccentricity(out, graph);
        printCanonicalLevelCode(out, graph);
        printIsomorphicVertices(out, graph);

        out.close();
    }

    private void printEccentricity(PrintWriter out, DrawGraph graph) {
        graph.calcEccentricity();
        int[] e = graph.e;
        out.println("Эксцентриситеты вершин:");
        for (int i = 0; i < graph.n; i++) {
            out.println(i + 1 + ": " + e[i]);
        }
        out.println();

        out.println("r(g) = " + graph.radius);
        out.println("d(g) = " + graph.diameter);

        if (graph.centralVertices.size() > 1) {
            out.print("Центры графа: {");
            Iterator<Integer> iterator = graph.centralVertices.iterator();
            while (iterator.hasNext()) {
                out.print(iterator.next() + 1);
                if (iterator.hasNext()) {
                    out.print(", ");
                }
            }
        }
        else {
            out.print("Центр графа = {" + (graph.centralVertices.get(0) + 1));
        }
        out.print("}\n\n");
    }

    private void printCanonicalLevelCode(PrintWriter out, DrawGraph graph) {
        String path = graph.getCanonicalLevelCode(out);
        out.println("Главный канонический уровневый код: " + path + "\n");
    }

    private void printIsomorphicVertices(PrintWriter out, DrawGraph graph) {
        out.println(graph.getIsomorphicVertices());
    }

    public static void main(String[] args) throws FileNotFoundException {
        new PruferCodeToTree().solve();
    }
}
