package graph;

import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    List<List<Pair<Integer, Integer>>> g = new ArrayList<>();
    public int n, m;

    public Graph(int n, int m) {
        this.n = n;
        this.m = m;

        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
    }

    public void addEdge(int v, int w, int c) {
        g.get(v).add(new Pair(w, c));
        g.get(w).add(new Pair(v, c));
    }

    public List<Pair<Integer, Integer>> getAdjacentEdges(int v) {
        return g.get(v);
    }
}


