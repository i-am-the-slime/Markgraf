package de.markeibes.markgraf;

public class DataPoint<A,B> {
    public final A label;
    public final B value;
    public DataPoint(A label, B value){
        this.label = label;
        this.value = value;
    }
}
