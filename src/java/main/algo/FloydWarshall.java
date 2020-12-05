package algo;

import graph.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class FloydWarshall {
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
        int n = g.n;
        int[][] dd = g.dd;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dd[i][k] < INF && dd[k][j] < INF) {
                        dd[i][j] = Math.min(dd[i][j], dd[i][k] + dd[k][j]);
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (dd[i][k] < INF && dd[k][j] < INF && dd[k][k] < 0) {
                        dd[i][j] = -INF;
                    }
                }
            }
        }

        write(g.dd);
    }

    void write(int[][] d) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(new FileOutputStream(outputFile), true);
        for (int i = 0, n = d.length; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String distance = (d[i][j] != INF) ? (d[i][j] != -INF) ? String.valueOf(d[i][j]) : "Negative cycle" : "No";
                out.print(distance + " ");
            }
            out.println();
        }
        out.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new FloydWarshall().readThenSolve();
    }
}