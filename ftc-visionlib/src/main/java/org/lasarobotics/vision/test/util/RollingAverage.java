package org.lasarobotics.vision.test.util;

import java.util.LinkedList;

/**
 * Structure that performs a continuous rolling average on values
 * Uses doubles as internal structures
 */
public class RollingAverage<T extends Number> {
    private final LinkedList<T> list;
    private int capacity;
    private double total;

    public RollingAverage(int capacity) {
        list = new LinkedList<>();
        this.capacity = capacity;
        total = 0;
    }

    public void addValue(T value) {
        list.addFirst(value);
        total += Double.valueOf(value.toString());
        trim();
    }

    private void trim() {
        while (list.size() > capacity) {
            total -= Double.valueOf(list.removeLast().toString());
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void clear() {
        list.clear();
    }

    public int getSize() {
        return list.size();
    }

    public double getAverage() {
        return (list.size() > 0) ? (total / list.size()) : 0;
    }

    public double getTotal() {
        return total;
    }
}