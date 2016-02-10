package org.whuims.commons.util;

public class Pair implements Comparable<Pair> {
    String key;
    double value;

    public Pair(String key, double value) {
        super();
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int compareTo(Pair o) {
        if (this.getValue() > o.getValue()) {
            return -1;
        } else if (this.getValue() < o.getValue()) {
            return 1;
        }
        return 0;
    }

}
