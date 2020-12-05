package algo;

import graph.Graph;
import utils.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Prim {
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
        int[] minEdge = new int[g.n];
        int[] selEdge = new int[g.n];
        boolean[] used = new boolean[g.n];
        Arrays.fill(minEdge, INF);
        Arrays.fill(selEdge, -1);
        minEdge[0] = 0;

        List<Pair<Integer, Pair<Integer, Integer>>> edges = new ArrayList<>();
        for (int i = 0; i < g.n; i++) {
            int v = -1;
            for (int j = 0; j < g.n; j++) {
                if (!used[j] && (v == -1 || minEdge[j] < minEdge[v])) {
                    v = j;
                }
            }
            if (minEdge[v] == -1) {
                write(null);
            }

            used[v] = true;
            if (selEdge[v] != -1) {
                edges.add(new Pair<>(v, new Pair<>(selEdge[v], minEdge[v])));
            }
            for (var vertex : g.getAdjacentEdges(v)) {
                int w = vertex.first;
                int cost = vertex.second;
                if (cost < minEdge[w]) {
                    minEdge[w] = cost;
                    selEdge[w] = v;
                }
            }
        }
        write(edges);
    }

    void write(List<Pair<Integer, Pair<Integer, Integer>>> edges) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(new FileOutputStream(outputFile), true);

        if (edges == null) {
            out.println("Нельзя построить MST!");
            out.close();
            return;
        }

        long sum = 0;
        for (int i = 0, n = edges.size(); i < n; i++) {
            sum += edges.get(i).second.second;
        }

        out.println(sum);
        for (int i = 0, n = edges.size(); i < n; i++) {
            var edge = edges.get(i);
            out.println((edge.second.first + 1) + " " + (edge.first + 1) + " " + edge.second.second);
        }

        out.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new Prim().readThenSolve();
    }
}
