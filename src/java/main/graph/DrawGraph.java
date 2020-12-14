package graph;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import utils.Pair;

import java.io.PrintWriter;
import java.util.*;

public class DrawGraph {
    static int edgeNumber = 0;
    static final int INF = Integer.MAX_VALUE;

    private Graph graph;

    public List<Pair<Integer, Pair<Integer, Integer>>> edges = new ArrayList<>();
    public List<List<Pair<Integer, Integer>>> g = new ArrayList<>();
    public List<Integer> centralVertices = new ArrayList<>();
    public int[][] dd;
    public int[] d, degree, e, p;
    public boolean[] used;
    public int diameter = -INF;
    public int radius = INF;
    public int n, m;
    private int v;

    public void addEdge(int v, int w) {
        addEdge(v, w, 1);
    }

    public void addEdge(int v, int w, int c) {
        g.get(v).add(new Pair<>(w, c));
        dd[v][w] = c;
        edges.add(new Pair<>(v, new Pair<>(w, c)));
        p[w] = v;

        g.get(w).add(new Pair<>(v, c));
        dd[w][v] = c;
        edges.add(new Pair<>(w, new Pair<>(v, c)));

        degree[v]++;
        degree[w]++;
        graph.addEdge(String.valueOf(edgeNumber++), String.valueOf(v + 1), String.valueOf(w + 1));
    }

    public void addVisualEdge(int v, int w) {
        String e = String.valueOf(edgeNumber++);
        graph.addEdge(e, String.valueOf(v + 1), String.valueOf(w + 1));
        var edge = graph.getEdge(e);
        edge.setAttribute("ui.style", getNewEdgeStyle());
    }

    public void addVertex(int v) {
        graph.addNode(String.valueOf(v));
    }

    public void addVisualVertex(int v) {
        String vv = String.valueOf(v);
        graph.addNode(vv);
        var node = graph.getNode(vv);
        node.setAttribute("ui.label", node.getId());
        node.setAttribute("ui.style", getNewVertexStyle());
    }

    public void buildGraphFromSequence(List<Integer> d, PrintWriter out) {
        List<Integer> vec = new ArrayList<>(d);
        List<Pair<Integer, Integer>> dp = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dp.add(new Pair<>(vec.get(i), i));
        }

        try {
            tryToBuildHamiltonianPath(vec);
        } catch (Exception e) {
            out.println("Строим граф другим образом...");
            buildGraphInCommonWay(dp);
        }
    }

    public void calcEccentricity() {
        floyd();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (dd[i][j] != INF) {
                    e[i] = Math.max(dd[i][j], e[i]);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            radius = Math.min(radius, e[i]);
            diameter = Math.max(diameter, e[i]);
        }
        for (int i = 0; i < n; i++) {
            if (e[i] == radius) {
                centralVertices.add(i);
            }
        }
    }

    @Override
    public DrawGraph clone() {
        DrawGraph cloned = new DrawGraph(n, m);
        boolean[][] uu = new boolean[n][n];
        for (var edge : edges) {
            int v = edge.first;
            int w = edge.second.first;
            int c = edge.second.second;
            if (!uu[v][w] && !uu[w][v]) {
                cloned.addEdge(v, w, c);
                uu[v][w] = uu[w][v] = true;
            }
        }
        return cloned;
    }

    public void draw() {
        graph.display();
    }

    public void drawWithDegrees() {
        overrideLabelsWithDegrees();
        draw();
    }

    public List<Pair<Integer, Integer>> getAdjacentEdges(int v) {
        return g.get(v);
    }

    public String getCanonicalLevelCode(PrintWriter out) {
        int c;
        if (centralVertices.size() == 2) {
            out.print("В графе 2 центра. ");
            int v = centralVertices.get(0);
            int w = centralVertices.get(1);

            deleteEdge(v, w);
            used = new boolean[n];
            var treeV = makeTree(v, new TreeNode(v));
            var treeW = makeTree(w, new TreeNode(w));
            restoreEdge(v, w);

            int sizeV = treeV.getSize();
            int sizeW = treeW.getSize();

            if (sizeV < sizeW) {
                out.print("В поддереве с корнем v = " + (v + 1) + " меньше вершин " +
                        "(" + sizeV + "<" + sizeW + "), поэтому выберем его корнем.\n");
                c = v;
            } else if (sizeW < sizeV) {
                out.print("В поддереве с корнем w = " + (w + 1) + " меньше вершин " +
                        "(" + sizeW + "<" + sizeV + "), поэтому выберем его корнем.\n");
                c = w;
            } else {
                var pathV = formPathForMainCanonicalCode(treeV, v, new ArrayList<>());
                var pathW = formPathForMainCanonicalCode(treeW, w, new ArrayList<>());
                if (comparePaths(pathV, pathW) == 1) {
                    out.println("Канонический код дерева с центром в v = " + (v + 1) +
                            " предшествует аналогичному в w = " + w + ", ");
                    out.println(pathV + " < " + pathW);
                    c = v;
                } else {
                    out.println("Канонический код дерева с центром в w = " + (w + 1) +
                            " не превосходит аналогичный в v = " + v + ", ");
                    out.println(pathW + " <= " + pathV);
                    c = w;
                }
            }
        } else {
            c = centralVertices.get(0);
        }

        used = new boolean[n];
        var tree = makeTree(c, new TreeNode(c));
        return listOfIntsToString(formPathForMainCanonicalCode(tree, c, new ArrayList<>()));
    }

    public String getIsomorphicVertices() {
        Set<Pair<Integer, Integer>> foundVertices = new TreeSet<>();
        Set<String> foundEdges = new TreeSet<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && degree[i] == degree[j]) {
                    var adjToI = g.get(i);
                    var adjToJ = g.get(j);
                    Collections.sort(adjToI);
                    Collections.sort(adjToJ);
                    if (adjToI.equals(adjToJ)) {
                        Pair<Integer, Integer> pair;

                        if (i <= j) {
                            pair = new Pair<>(i + 1, j + 1);
                        } else {
                            pair = new Pair<>(j + 1, i + 1);
                        }
                        foundVertices.add(pair);

                        adjToI.retainAll(adjToJ);
                        for (Pair<Integer, Integer> w : adjToI) {
                            String edge1 = "(" + (i + 1) + "-" + (w.first + 1) + ")";
                            String edge2 = "(" + (j + 1) + "-" + (w.first + 1) + ")";
                            foundEdges.add(edge1);
                            foundEdges.add(edge2);
                        }
                    }
                }
            }
        }

        StringBuilder res = new StringBuilder();
        res.append("Кандидаты на подобные вершины: {").append(setOfPairsToStr(foundVertices)).append("}\n");
        res.append("Кандидаты на подобные ребра: {").append(setOfStrsToStr(foundEdges)).append("}\n");

        return res.toString();
    }

    public List<Integer> formPathForMainCanonicalCode(TreeNode subTree, int root, List<Integer> path) {
        List<List<Integer>> allPathsSorted = new ArrayList<>();
        for (TreeNode child : subTree.getChildren()) {
            List<Integer> branchPath = formPathForMainCanonicalCode(child, root, new ArrayList<>());
            allPathsSorted.add(branchPath);
        }

        allPathsSorted.sort(this::comparePaths);
        for (var branchPath : allPathsSorted) {
            path.addAll(branchPath);
        }
        path.add(dd[root][subTree.getKey()] + 1);
        return path;
    }

    public boolean isAdjacent(int v, int w) {
        return g.get(v).contains(w) || g.get(w).contains(v);
    }

    public void minimalEdgeExpansion(PrintWriter out) {
        int minDegree = INF, maxDegree = -INF;
        for (int i = 0; i < n; i++) {
            minDegree = Math.min(minDegree, degree[i]);
            maxDegree = Math.max(maxDegree, degree[i]);
        }
        List<Integer> minDegVertices = new ArrayList<>();
        int countMax = 0;
        for (int i = 0; i < n; i++) {
            if (degree[i] == minDegree) {
                minDegVertices.add(i);
            }
            if (degree[i] == maxDegree) {
                countMax++;
            }
        }

        if (countMax == n) {
            out.println("Граф полный. Построить реберное расширение нельзя.");
            return;
        }

        List<Pair<Integer, Integer>> edges = new ArrayList<>();
        int countMin = minDegVertices.size();
        boolean[] uu = new boolean[n];
        for (int i = 0; i < countMin; i++) {
            int v = minDegVertices.get(i);
            if (uu[v]) {
                continue;
            }
            int w = -1;

            for (int j = 0; j < countMin; j++) {
                int ww = minDegVertices.get(j);
                if (i != j && !isAdjacent(v, ww) && !uu[v] && !uu[ww]) {
                    w = ww;
                    uu[v] = true;
                    uu[w] = true;
                    break;
                }
            }

            if (w == -1) {
                for (int j = 0; j < n; j++) {
                    if (v != j && !isAdjacent(v, j)) {
                        w = j;
                        break;
                    }
                }
            }

            edges.add(new Pair<>(v, w));
        }

        int edgesShouldBeAdded = edges.size();
        out.println("В данном графе " + countMin + " вершин с минимальной степенью = " + minDegree + ". " +
                "Минимальное реберное расширение потребовало добавления " + edgesShouldBeAdded + " ребер.");

        for (var pair : edges) {
            addVisualEdge(pair.first, pair.second);
            out.println((pair.first + 1) + " " + (pair.second + 1));
        }
    }

    public void minimalVertexExpansion(PrintWriter out) {
        int minDegree = INF, maxDegree = -INF;
        for (int i = 0; i < n; i++) {
            minDegree = Math.min(minDegree, degree[i]);
            maxDegree = Math.max(maxDegree, degree[i]);
        }
        List<Integer> minDegVertices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] == minDegree) {
                minDegVertices.add(i);
            }
        }

        int countMin = minDegVertices.size();
        int edgesCount = Math.max(countMin, maxDegree);
        out.println("В данном графе " + countMin + " вершин с минимальной степенью = " + minDegree +
                " и максимальная степень = " + maxDegree + ".");
        out.println("Минимальное вершинное расширение потребует добавления " + edgesCount + " ребер.");

        int w = n;
        addVisualVertex(w + 1);
        used = new boolean[n];
        for (int i = 0; i < countMin; i++) {
            int v = minDegVertices.get(i);
            used[v] = true;
            addVisualEdge(v, w);
            out.println((v + 1) + " " + (w + 1));
        }

        if (countMin < edgesCount) {
            for (int i = 0; i < n; i++) {
                if (!used[i]) {
                    used[i] = true;
                    addVisualEdge(i, w);
                    out.println((i + 1) + " " + (i + 1));
                }
            }
        }
    }

    private boolean areAllPairsAreZero(List<Pair<Integer, Integer>> dp) {
        for (var p : dp) {
            if (p.first != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean areAllZero(List<Integer> d) {
        for (Integer p : d) {
            if (p != 0) {
                return false;
            }
        }
        return true;
    }

    private void buildGraphInCommonWay(List<Pair<Integer, Integer>> dp) {
        System.out.println("\nСтроим другим образом...");
        initFrontEnd();
        int v, x;

        while (!areAllPairsAreZero(dp)) {
            dp.sort(Comparator.reverseOrder());
            for (v = 0; v < dp.size(); v++) {
                x = dp.get(v).first;
                if (x > 0) {
                    break;
                }
            }
            deleteElementFromSequencePair(dp, dp.get(v), v);
        }
        System.out.println();
    }

    private int comparePaths(List<Integer> path1, List<Integer> path2) {
        int n = path1.size();
        int m = path2.size();
        int i = 0, j = 0;
        while (i < n && j < m) {
            int equal = path1.get(i++).compareTo(path2.get(j++));
            if (equal != 0) {
                return -equal;
            }
        }
        if (i < n) {
            return -1;
        }
        if (j < m) {
            return 1;
        }
        return 0;
    }

    private void deleteEdge(int v, int w) {
        g.get(v).remove(new Pair<>(w, 1));
        g.get(w).remove(new Pair<>(v, 1));
    }

    private int deleteElementFromSequence(List<Integer> d, int v, int degree) {
        d.set(v, 0);
        int w = 0;
        int minAdjVert = INF, minDegree = INF;
        while (degree > 0) {
            if (w == v || d.get(w) == 0) {
                w++;
                continue;
            }

            d.set(w, d.get(w) - 1);
            if (d.get(w) != 0 && minDegree > d.get(w)) {
                minAdjVert = w;
                minDegree = d.get(w);
            }
            addEdge(v, w++);
            degree--;
        }

        for (int i = 0; i < d.size(); i++) {
            System.out.print(d.get(i) + " ");
        }
        System.out.println();
        return minAdjVert != INF ? minAdjVert : w;
    }

    private int deleteElementFromSequencePair(List<Pair<Integer, Integer>> dp, Pair<Integer, Integer> v, int pos) {
        int degree = v.first;
        dp.set(pos, new Pair<>(0, pos));
        dp.sort(Comparator.reverseOrder());
        int w = 0;
        int minAdjVert = INF, minDegree = INF;

        while (degree > 0) {
            if (dp.get(w).second.equals(v.second) || dp.get(w).first == 0) {
                w++;
                continue;
            }

            dp.set(w, new Pair<>(dp.get(w).first - 1, dp.get(w).second));
            if (dp.get(w).first != 0 && minDegree > dp.get(w).first) {
                minAdjVert = dp.get(w).second;
                minDegree = dp.get(w).first;
            }
            addEdge(v.second, dp.get(w++).second);
            degree--;
        }

        for (int i = 0; i < dp.size(); i++) {
            System.out.print(dp.get(i).first + " ");
        }
        System.out.println();
        return minAdjVert != INF ? minAdjVert : w;
    }

    private void floyd() {
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
    }

    private String getNewEdgeStyle() {
        return "fill-color: red;";
    }

    private String getNewVertexStyle() {
        return "fill-color: red; " +
                "size: 20px; " +
                "text-alignment: under; " +
                "text-size: 20px;";
    }

    private String getStyle() {
        return "fill-color: blue; " +
                "size: 20px; " +
                "text-alignment: under; " +
                "text-size: 20px;";
    }

    private void initBackEnd(int n, int m) {
        this.n = n;
        this.m = m;

        dd = new int[n][n];
        d = new int[n];
        e = new int[n];
        degree = new int[n];
        p = new int[n];
        used = new boolean[n];

        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
            d[i] = INF;
            e[i] = -INF;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    dd[i][j] = INF;
                }
            }
        }
    }

    private void initFrontEnd() {
        System.setProperty("org.graphstream.ui", "swing");
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        graph = new SingleGraph("SingleGraph");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        for (int i = 0; i < n; i++) {
            addVertex(i + 1);
        }
        for (Node node : graph) {
            node.setAttribute("ui.label", node.getId());
            node.setAttribute("ui.style", getStyle());
        }
    }

    private String listOfIntsToString(List<Integer> path) {
        return path.toString().replace("[", "(").replace("]", ")");
    }

    private TreeNode makeTree(int v, TreeNode treeNode) {
        used[v] = true;
        for (var pair : g.get(v)) {
            int w = pair.first;
            if (!used[w]) {
                treeNode.addChild(w, v);
                makeTree(w, treeNode);
            }
        }
        return treeNode;
    }

    private void overrideLabelsWithDegrees() {
        for (Node node : graph) {
            node.setAttribute("ui.label", node.getDegree());
        }
    }

    private void restoreEdge(int v, int w) {
        g.get(v).add(new Pair<>(w, 1));
        g.get(w).add(new Pair<>(v, 1));
    }

    private <F extends Comparable<F>, S extends Comparable<S>> String setOfPairsToStr(Set<Pair<F, S>> set) {
        StringBuilder res = new StringBuilder();
        for (Pair<F, S> pair : set) {
            res.append(pair).append(", ");
        }
        if (res.lastIndexOf(",") != -1) {
            return res.substring(0, res.lastIndexOf(","));
        } else {
            return res.toString();
        }
    }

    private String setOfStrsToStr(Set<String> set) {
        StringBuilder res = new StringBuilder();
        for (String str : set) {
            res.append(str).append(", ");
        }
        if (res.lastIndexOf(",") != -1) {
            return res.substring(0, res.lastIndexOf(","));
        } else {
            return res.toString();
        }
    }

    private void tryToBuildHamiltonianPath(List<Integer> d) throws Exception {
        System.out.println("Пытаемся строить гамильтонов путь...");
        int v, x = 0, w = 0;
        while (!areAllZero(d)) {
            for (v = w; v >= 0; v--) {
                x = d.get(v);
                if (x > 0) {
                    break;
                }
            }
            w = deleteElementFromSequence(d, v, x);
        }
        System.out.println();
    }

    public DrawGraph(int n, int m) {
        initBackEnd(n, m);
        initFrontEnd();
    }
}
