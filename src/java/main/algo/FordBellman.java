package algo;

import graph.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class FordBellman {
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
            if (c >= 0) {
                g.addEdge(v, w, c);
            }
        }

        solve(g);
    }

    void solve(Graph g) throws FileNotFoundException {
        int[] d = g.d;
        d[0] = 0;
        int x = 0;

        for (int i = 0; i < g.n; i++) {
            x = -1;
            for (int j = 0, mm = g.edges.size(); j < mm; j++) {
                var edge = g.edges.get(j);
                int v = edge.first;
                int w = edge.second.first;
                int c = edge.second.second;

                if (d[v] < INF && c >= 0) {
                    if (d[w] > d[v] + c) {
                        d[w] = Math.max(-INF, d[v] + c);
                        x = w;
                    }
                }
            }
        }

        write(d, x);
    }

    void write(int[] d, int x) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(new FileOutputStream(outputFile), true);
        if (x != -1) {
            out.println("Graph contains negative cycle");
            out.close();
            return;
        }

        for (int i = 0, n = d.length; i < n; i++) {
            String distance = (d[i] != INF) ? String.valueOf(d[i]) : "No";
            out.println((i + 1) + " " + distance);
        }
        out.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new FordBellman().readThenSolve();
    }
}