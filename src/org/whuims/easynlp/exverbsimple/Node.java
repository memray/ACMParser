package org.whuims.easynlp.exverbsimple;

import java.util.Set;
import java.util.TreeSet;

public class Node<T> {
    private T value;
    private Set<Node<T>> sources = new TreeSet<Node<T>>();
    private Set<Node<T>> targets = new TreeSet<Node<T>>();

    public Node() {
        super();
    }

    public Node(T value) {
        super();
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Set<Node<T>> getSources() {
        return sources;
    }

    public void setSources(Set<Node<T>> sources) {
        this.sources = sources;
    }

    public Set<Node<T>> getTargets() {
        return targets;
    }

    public void setTargets(Set<Node<T>> targets) {
        this.targets = targets;
    }

}
