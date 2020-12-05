package algo;


import utils.DisjointSet;
import utils.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Kruscal {
    static final String inputFile = "input.txt";
    static final String outputFile = "output.txt";

    void readThenSolve() throws FileNotFoundException {
        Scanner in = new Scanner(new File(inputFile));
        int n = in.nextInt();
        int m = in.nextInt();
        List<Pair<Integer, Pair<Integer, Integer>>> edges = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            int v = in.nextInt() - 1;
            int w = in.nextInt() - 1;
            int c = in.nextInt();
            edges.add(new Pair<>(c, new Pair<>(v, w)));
        }

        Collections.sort(edges);
        solve(edges, n);
    }

    void solve(List<Pair<Integer, Pair<Integer, Integer>>> edges, int n) throws FileNotFoundException {
        long sum = 0;
        DisjointSet dSet = new DisjointSet(n);
        List<Pair<Integer, Pair<Integer, Integer>>> mst = new ArrayList<>();

        for (var edge : edges) {
            int c = edge.first;
            int v = edge.second.first;
            int w = edge.second.second;

            if (dSet.findSet(v) != dSet.findSet(w)) {
                sum += c;
                mst.add(new Pair<>(v, new Pair<>(w, c)));
                dSet.unionSets(v, w);
            }
        }

        write(sum, mst);
    }

    void write(long sum, List<Pair<Integer, Pair<Integer, Integer>>> edges) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(new FileOutputStream(outputFile), true);
        out.println(sum);
        for (int i = 0, n = edges.size(); i < n; i++) {
            var edge = edges.get(i);
            out.println((edge.first + 1) + " " + (edge.second.first + 1) + " " + edge.second.second);
        }
        out.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new Kruscal().readThenSolve();
    }
}
