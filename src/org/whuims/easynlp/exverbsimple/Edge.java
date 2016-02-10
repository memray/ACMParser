package org.whuims.easynlp.exverbsimple;

public class Edge<T, E> {
    T source;
    T target;
    E rel;

    public Edge() {
        super();
    }

    public Edge(T source, T target, E rel) {
        super();
        this.source = source;
        this.target = target;
        this.rel = rel;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public T getTarget() {
        return target;
    }

    public void setTarget(T target) {
        this.target = target;
    }

    public E getRel() {
        return rel;
    }

    public void setRel(E rel) {
        this.rel = rel;
    }

}
