package ar.edu.itba.ss.step.models;

import java.util.Objects;

public class Pair<T,V> {
    private final T value1;
    private final V value2;

    Pair(T value1, V value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public boolean has(Object value) {
        return value.equals(value1) || value.equals(value2);
    }

    public T getOne() {
        return value1;
    }

    public V getOther() {
        return value2;
    }

    public static <T,V> Pair<T,V> of(T value1, V value2) {
        return new Pair<>(value1, value2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair<?,?> pair)) return false;
        return (value1.equals(pair.value1) && value2.equals(pair.value2)) || (value1.equals(pair.value2) && value2.equals(pair.value1));

    }

    @Override
    public int hashCode() {
        return Objects.hash(value1.hashCode() + value2.hashCode());
    }
}
