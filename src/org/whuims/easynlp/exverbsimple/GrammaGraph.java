package org.whuims.easynlp.exverbsimple;

import java.util.List;

import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;

public class GrammaGraph<T, E> {
    private T root;
    private List<T> nodes;
    private Table<T, T, Edge<T, E>> relMap = HashBasedTable.create();

    public void addNode(T t) {
        this.nodes.add(t);
    }

    public void addRelation(T t1, T t2, E relString) {
        Edge<T, E> edge = new Edge<T, E>();
        edge.setSource(t1);
        edge.setTarget(t2);
        edge.setRel(relString);
        this.relMap.put(t1, t2, edge);
    }

    public static void main(String[] args) {
        GrammaGraph<Node<String>, String> graph = new GrammaGraph<Node<String>, String>();
        Node node1 = new Node<String>("paper");
        Node node2 = new Node<String>("The");
        Node node3 = new Node<String>("presents");
        Node node4 = new Node<String>("ROOT");
        graph.addNode(node1);
    }
}
