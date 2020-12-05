package algo;

import graph.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Dijkstra {
    static final int INF = Integer.MAX_VALUE;
    static final String inputFile = "input.txt";
    static final String outputFile = "output.txt";

    void readThenSolve() throws FileNotFoundException {
        Scanner in = new Scanner(new File(inputFile));
        int n = in.nextInt();
        int m = in.nextInt();
        Graph g = new Graph(n, m);

        for (int i = 0; i < m; i++) {
            int v = in.nextInt() - 1;
            int w = in.nextInt() - 1;
            int c = in.nextInt();
            g.addEdge(v, w, c);
        }

        solve(g);
    }

    void solve(Graph g) throws FileNotFoundException {
        int[] d = g.d;
        Queue<Integer> q = new PriorityQueue<>();
        d[0] = 0;
        q.add(0);

        while (!q.isEmpty()) {
            int v = q.poll();
            for (var el : g.getAdjacentEdges(v)) {
                int w = el.first;
                int c = el.second;
                if (d[v]+ c < d[w]) {
                    d[w] = d[v]+ c;
                    q.add(w);
                }
            }
        }

        write(d);
    }

    void write(int[] d) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(new FileOutputStream(outputFile), true);
        for (int i = 0, n = d.length; i < n; i++) {
            String distance = (d[i] != INF) ? String.valueOf(d[i]) : "No";
            out.println((i + 1) + " " + distance);
        }
        out.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new Dijkstra().readThenSolve();
    }
}