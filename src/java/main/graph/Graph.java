package graph;

import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    static final int INF = Integer.MAX_VALUE;

    public List<Pair<Integer, Pair<Integer, Integer>>> edges = new ArrayList<>();
    public List<List<Pair<Integer, Integer>>> g = new ArrayList<>();
    public int[][] dd;
    public int[] d;
    public int n, m;

    public Graph(int n, int m) {
        this.n = n;
        this.m = m;
        dd = new int[n][n];
        d = new int[n];

        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
            d[i] = INF;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    dd[i][j] = INF;
                }
            }
        }
    }

    public void addEdge(int v, int w, int c) {
        g.get(v).add(new Pair(w, c));
        dd[v][w] = c;
        edges.add(new Pair<>(v, new Pair<>(w, c)));

        g.get(w).add(new Pair(v, c));
        dd[w][v] = c;
        edges.add(new Pair<>(w, new Pair<>(v, c)));
    }

    public List<Pair<Integer, Integer>> getAdjacentEdges(int v) {
        return g.get(v);
    }

    public List<Pair<Integer, Pair<Integer, Integer>>> getEdges() {
        return edges;
    }

    ;
}


