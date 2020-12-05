package utils;

public class DisjointSet {
    int[] parent;
    int[] rank;
    int n;

    public int findSet(int v) {
        if (v == parent[v]) {
            return v;
        }
        parent[v] = findSet(parent[v]);
        return parent[v];
    }

    public void unionSets(int a, int b) {
        a = findSet(a);
        b = findSet(b);
        if (a != b) {
            if (rank[a] < rank[b]) {
                a ^= b;
                b ^= a;
                a ^= b;
            }
            parent[b] = a;
            if (rank[a] == rank[b]) {
                rank[a]++;
            }
        }
    }

    public DisjointSet(int n) {
        this.n = n;
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
}
