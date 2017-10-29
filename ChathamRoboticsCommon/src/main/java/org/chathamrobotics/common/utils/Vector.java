package org.chathamrobotics.common.utils;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Arrays;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 10/29/2017
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Vector {

    /**
     * Adds the given vectors together
     * @param vectors   the vectors to add together
     * @return          the result of the vector addition
     */
    public static Vector add(Vector ...vectors) {
            return Arrays.stream(vectors)
                    .map(vector -> vector.components)
                    .sorted((v1, v2) -> v1.length > v2.length ? 1 : -1)
                    .reduce((v1, v2) -> {
                        double[] comps = new double[v1.length];

                        for (int i = 0; i < v1.length; i++)
                            comps[i] = v1[i] + (i < v2.length ? v2[i] : 0);

                        return comps;
                    })
                    .map(Vector::new)
                    .orElse(null);
    }

    /**
     * Performs a dot product on the given vectors
     * @param v1    the first vector
     * @param v2    the second vector
     * @return      the dot product of the two vectors
     */
    public static double dotProduct(Vector v1, Vector v2) {
        double result = 0;

        for (int i = 0; i < v1.components.length; i++) {
            result += v1.components[i]
                    * ( i < v2.components.length ? v2.components[i] : 0);
        }

        return result;
    }

    /**
     * Finds the angle between two vectors
     * @param v1    the first vector
     * @param v2    the second vector
     * @return      the angle between the two vectors in radians
     */
    public static double angleBetween(Vector v1, Vector v2) {
        return angleBetween(v1, v2, AngleUnit.RADIANS);
    }

    /**
     * Finds the angle between two vectors
     * @param v1        the first vector
     * @param v2        the second vector
     * @param angleUnit the units to use for the angle measurement
     * @return          the angle between the two vectors
     */
    public static double angleBetween(Vector v1, Vector v2, AngleUnit angleUnit) {
        return angleUnit.fromRadians(Math.acos(dotProduct(v1, v2) / (v1.magnitude() * v2.magnitude())));
    }

    /**
     * Projects the first vector onto the second
     * @param v1    the first vector
     * @param v2    the second vector
     * @return      the projected vector
     */
    public static Vector project(Vector v1, Vector v2) {
        return v2.scalarMultiply(dotProduct(v1, v2) / Math.pow(v2.magnitude(), 2));
    }

    private final double[] components;

    /**
     * Creates a new instance of {@link Vector}
     * @param components    the vector's components
     */
    public Vector(double ...components) {
        this.components = components;
    }

    /**
     * Gets the size of the vector (the number of components)
     * @return  the size of the vector
     */
    public int size() {
        return components.length;
    }

    /**
     * Returns the component at the given index
     * @param index the index of the component to get
     * @return      the component
     */
    public double get(int index) {
        return components[index];
    }

    /**
     * Sets the value of the component at the given index
     * @param index the index of the component whose value is to be set
     * @param val   the value to set
     */
    public void set(int index, double val) {
        components[index] = val;
    }

    /**
     * Scalar multiplies the vector by the given scalar quantity
     * @param scalar    the scalar quantity
     * @return          the resulting vector
     */
    public Vector scalarMultiply(double scalar) {
        double[] comps = Arrays.copyOf(components, components.length);

        for (int i = 0; i < comps.length; i++) {
            comps[i] *= scalar;
        }

        return new Vector(comps);
    }

    /**
     * Returns the unit vector for this vector
     * @return  the unit vector
     */
    public Vector getUnit() {
        return scalarMultiply(1/magnitude());
    }

    /**
     * Returns the opposite of the vector
     * @return  the opposite of the vector
     */
    public Vector opposite() {
        return scalarMultiply(-1);
    }

    /**
     * Returns the magnitude of the vector
     * @return  the magnitude of the vector
     */
    public double magnitude() {
        double sum = 0;

        for (Number component : components) {
            sum += Math.pow(component.doubleValue(), 2);
        }

        return Math.sqrt(sum);
    }

    /**
     * Returns a array of angles in radians representing the angles between each of the components
     * and their corresponding unit vectors
     * @return  the direction of the vector
     */
    public double[] direction() {
        return direction(AngleUnit.RADIANS);
    }

    /**
     * Returns a array of angles representing the angles between each of the components
     * and their corresponding unit vectors
     * @return  the direction of the vector
     */
    public double[] direction(AngleUnit angleUnit) {
        double mag = magnitude();
        double[] result = new double[components.length];

        for (int i = 0; i < components.length; i++) {
            result[i] = angleUnit.fromRadians(Math.acos(components[i] / mag));
        }

        return result;
    }
}
