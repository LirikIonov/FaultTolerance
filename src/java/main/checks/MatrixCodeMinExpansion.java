package checks;

import graph.DrawGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

public class MatrixCodeMinExpansion {
    static final String inputFile = "input.txt";
    static final String outputFile = "output.txt";

    static final Set<Character> notDigits = Set.of(',', '(', ')', ' ', '.', '{', '}');

    DrawGraph readGraph() throws FileNotFoundException {
        Scanner in = new Scanner(new File(inputFile));
        String[] vec = in.nextLine().split(" ");
        int sz = vec.length;
        int n = vec[sz - 1].length();
        int[][] m = new int[n + 1][n + 1];
        int edgeCnt = 0;
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < vec[i].length(); j++) {
                if (vec[i].charAt(j) - '0' == 1) {
                    edgeCnt++;
                    m[i + 1][j] = 1;
                }
            }
        }

        for (int i = 0; i < sz + 1; i++) {
            System.out.println(Arrays.toString(m[i]).replace("[", "")
                    .replace("]", "").replace(",", ""));
        }


        DrawGraph graph = new DrawGraph(n + 1, edgeCnt);
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < n + 1; j++) {
                if (m[i][j] == 1) {
                    graph.addEdge(i, j, 1);
                }
            }
        }

        return graph;
    }

    void solveAndPrint() throws FileNotFoundException {
        DrawGraph graph = readGraph();
        graph.draw();
        PrintWriter out = new PrintWriter(new FileOutputStream(outputFile), true);

        makeMinimalEdgeExpansion(graph, out);
        makeMinimalVertexExpansion(graph, out);

        out.close();
    }

    void makeMinimalEdgeExpansion(DrawGraph graph, PrintWriter out) throws FileNotFoundException {
        DrawGraph graph2 = graph.clone();
        graph2.minimalEdgeExpansion(out);
        graph2.draw();
    }

    void makeMinimalVertexExpansion(DrawGraph graph, PrintWriter out) throws FileNotFoundException {
        DrawGraph graph2 = graph.clone();
        graph2.minimalVertexExpansion(out);
        graph2.draw();
    }



    public static void main(String[] args) throws FileNotFoundException {
        new MatrixCodeMinExpansion().solveAndPrint();
    }
}