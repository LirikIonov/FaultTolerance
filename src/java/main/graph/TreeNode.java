package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeNode {
    private List<TreeNode> children = new ArrayList<>();
    private int key, size;

    public TreeNode(int key) {
        this.key = key;
        this.size = 1;
    }

    public void addChild(int key, int ancestor) {
        if (ancestor == this.key) {
            children.add(new TreeNode(key));
            return;
        }

        var listOfNodes = dfs(this, new ArrayList<>());
        Collections.reverse(listOfNodes);
        for (TreeNode v : listOfNodes) {
            if (ancestor == v.getKey()) {
                v.getChildren().add(new TreeNode(key));
                return;
            }
        }
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public int getKey() {
        return key;
    }

    public int getSize() {
        size = 1;
        for (TreeNode child : children) {
            size += child.getSize();
        }
        return size;
    }

    public void printTree() {
        var path = dfs(this, new ArrayList<>());
        Collections.reverse(path);
        for (TreeNode v : path) {
            System.out.print(v.getKey() + 1 + " ");
        }
    }

    public void setKey(int key) {
        this.key = key;
    }

    private List<TreeNode> dfs(TreeNode v, List<TreeNode> path) {
        for (var w : v.children) {
            path = dfs(w, path);
        }
        path.add(v);
        return path;
    }
}
