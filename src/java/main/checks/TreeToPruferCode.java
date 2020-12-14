package checks;

import graph.DrawGraph;
import utils.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TreeSet;

public class TreeToPruferCode {
    static final String inputFile = "input.txt";
    static final String outputFile = "output.txt";

    public void readAndSolve() throws FileNotFoundException {
        Scanner in = new Scanner(new File(inputFile));
        int n = in.nextInt();
        int m = in.nextInt();
        DrawGraph g = new DrawGraph(n, m);

        for (int i = 0; i < m; i++) {
            int v = in.nextInt() - 1;
            int w = in.nextInt() - 1;
            g.addEdge(v, w, 1);
        }

        solve(g);
    }

    public void solve(DrawGraph g) throws FileNotFoundException {
        int n = g.n;
        TreeSet<Integer> leafs = new TreeSet<>();
        int[] degree = g.degree;
        boolean[] used = new boolean[n];
        for (int i = 0; i < g.n; i++) {
            if (degree[i] == 1) {
                leafs.add(i);
            }
        }

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n - 2; i++) {
            int leaf = leafs.pollFirst();
            used[leaf] = true;

            int adjToLeaf = -1;
            for(Pair<Integer, Integer> el : g.getAdjacentEdges(leaf)) {
                int w = el.first;
                if (!used[w]) {
                    adjToLeaf = w;
                }
            }

            s.append(adjToLeaf + 1 + " ");
            if (--degree[adjToLeaf] == 1) {
                leafs.add(adjToLeaf);
            }
        }
        write(s.toString());
    }

    public void write(String s) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(new FileOutputStream(outputFile), true);
        out.print("(");
        String[] split = s.split(" ");

        for (int i = 0, len = split.length; i < len; i++) {
            out.print(split[i]);
            if (i != split.length - 1) {
                out.print(",");
            }
        }
        out.println(")");
        out.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new TreeToPruferCode().readAndSolve();
    }
}
