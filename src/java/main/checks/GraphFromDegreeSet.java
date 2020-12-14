package checks;

import graph.DrawGraph;
import utils.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class GraphFromDegreeSet {
    static final String inputFile = "input.txt";
    static final String outputFile = "output.txt";

    static final Set<Character> notDigits = Set.of(',', '(', ')', ' ', '.', '{', '}');


    void solveAndPrint() throws FileNotFoundException {
        Scanner in = new Scanner(new File(inputFile));

        String vec = in.nextLine();
        TreeSet<Integer> s = new TreeSet<>();
        int start = 0;
        if ((vec.contains(".") && vec.indexOf(".") < vec.length() - 1)) {
            start = vec.indexOf('.') + 1;
        }

        for (int len = vec.length(); start < len; ) {
            char c = vec.charAt(start);
            if (notDigits.contains(c)) {
                ++start;
                continue;
            }

            String str = "";
            while (!notDigits.contains(c) && start++ < len) {
                str += c;
                if (start < len) {
                    c = vec.charAt(start);
                }
            }
            s.add(Integer.parseInt(str));
        }

        var g= formDegreeSequence(s);
        printGraph(g);
    }

    private void printGraph(List<Set<Integer>> g) {
        int n = g.size();
        int m = 0;
        for (var list : g) {
            m += list.size();
        }
        DrawGraph graph = new DrawGraph(n, m);

        Set<Pair<Integer, Integer>> addedEdges = new HashSet<>();
        for (int i = 0; i < n; i++) {
            for (var j : g.get(i)) {
                if (!addedEdges.contains(new Pair<>(i, j))) {
                    graph.addEdge(i, j, 1);
                    addedEdges.add(new Pair<>(i, j));
                    addedEdges.add(new Pair<>(j, i));
                }
            }
        }

        graph.drawWithDegrees();
    }

    private void addFullyConnectedVertex(int idx, int degree, int maxDegree,
                                         List<Integer> d, List<Boolean> u, List<Set<Integer>> g) {
        int gSize = g.size();
        for (int i = idx + 1; i < gSize && degree > 0; i++) {
            if (!g.get(i).contains(idx) && !u.get(i) && d.get(i) < maxDegree) {
                addEdge(idx, i, maxDegree, g, d, u);
                degree--;
            }
        }

        while (degree > 0) {
            addEdge(idx, gSize, maxDegree, g, d, u);
            gSize++;
            degree--;
        }
    }

    private void addEdge(int v, int w, int maxDegree,
                         List<Set<Integer>> g, List<Integer> d, List<Boolean> u) {
        if (w == g.size()) {
            g.add(new TreeSet<>());
            d.add(0);
            u.add(false);
        }

        g.get(v).add(w);
        g.get(w).add(v);
        //d.set(v, d.get(v) + 1);
        d.set(w, d.get(w) + 1);

        if (d.get(w) == maxDegree) {
            u.set(w, true);
        }
    }

    private int graphSequenceEqualToSet(Set<Integer> s, List<Set<Integer>> g) {
        for (int i = 0, gSize = g.size(); i < gSize; i++) {
            if (!s.contains(g.get(i).size())) {
                return i;
            }
        }
        return -1;
    }

    private List<Set<Integer>> formDegreeSequence(TreeSet<Integer> s) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(new FileOutputStream(outputFile), true);
        List<Integer> d = new ArrayList<>();
        List<Boolean> u = new ArrayList<>();
        List<Set<Integer>> g = new ArrayList<>();
        phase1(s, d, u, g);
        phase2(s, d, u, g);

        d.sort(Comparator.reverseOrder());
        out.println(d.toString().replace("[", "(").replace("]", ")")
                .replace(" ", ""));

        int i = 0;
        for (var v : g) {
            System.out.println(++i + ": " + setToStr(v));
        }
        out.close();
        return g;
    }

    private void phase1(TreeSet<Integer> s, List<Integer> d, List<Boolean> u, List<Set<Integer>> g) {
        int maxDegree = s.last();
        for (Integer e : s) {
            int idx = 0;
            int degree = e;
            boolean reuse = false;
            for (int sz = d.size(); idx < sz; idx++) {
                if (!u.get(idx) && d.get(idx) < e) {
                    degree = e - d.get(idx);
                    d.set(idx, e);
                    u.set(idx, true);
                    reuse = true;
                    break;
                }
            }

            if (!reuse) {
                d.add(e);
                u.add(true);
                g.add(new TreeSet<>());
            }

            addFullyConnectedVertex(idx, degree, maxDegree, d, u, g);
        }
    }



    private void phase2(TreeSet<Integer> s, List<Integer> d, List<Boolean> u, List<Set<Integer>> g) {
        int maxDegree = s.last(), idx;
        while ((idx = graphSequenceEqualToSet(s, g)) != -1) {
            int idxDeg = g.get(idx).size();
            int neededDeg = s.higher(idxDeg);
            d.set(idx, neededDeg);
            int degree = neededDeg - idxDeg;
            addFullyConnectedVertex(idx, degree, maxDegree, d, u, g);
        }
    }

    private String setToStr(Set<Integer> set) {
        StringBuilder res = new StringBuilder();
        for (int e : set) {
            res.append(e + 1).append(" ");
        }
        return res.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new GraphFromDegreeSet().solveAndPrint();
    }
}
