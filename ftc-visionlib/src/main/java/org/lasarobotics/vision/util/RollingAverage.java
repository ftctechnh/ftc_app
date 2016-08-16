/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 *
 * Thank you to FTCLib contributors.
 */
package org.lasarobotics.vision.util;

import java.util.LinkedList;

/**
 * Structure that performs a continuous rolling average on values
 * Uses doubles as internal structures
 */
class RollingAverage<T extends Number> {
    private final LinkedList<T> list;
    private int capacity;
    private double total;

    /**
     * Initialize a rolling average based with a specific capacity
     *
     * @param capacity Capacity of the rolling average
     */
    public RollingAverage(int capacity) {
        list = new LinkedList<>();
        this.capacity = capacity;
        total = 0;
    }

    /**
     * Add a value to the averager
     *
     * @param value Value to add
     */
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

    /**
     * Get the capacity of the averager
     *
     * @return Capacity of the averager
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Set a capacity of the averager
     *
     * @param capacity New capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Remove and zero all elements
     */
    public void clear() {
        list.clear();
    }

    /**
     * Get the current count of the items in the underlying array
     *
     * @return Current count of items in the averaging array
     */
    public int getSize() {
        return list.size();
    }

    /**
     * Get the average of the items currently in the array
     *
     * @return Average
     */
    public double getAverage() {
        return (list.size() > 0) ? (total / list.size()) : 0;
    }

    /**
     * Get the sum of the numbers in the array
     *
     * @return Sum of values
     */
    public double getTotal() {
        return total;
    }
}