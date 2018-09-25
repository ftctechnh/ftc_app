package com.acmerobotics.roadrunner.util;

import java.util.*

/**
 * Interpolating Tree Maps are used to get values at points that are not defined by making a guess from points that are
 * defined. This uses linear interpolation.
 *
 * Credit to FRC Team 254: [InterpolatingTreeMap.java](https://github.com/Team254/FRC-2017-Public/blob/master/src/com/team254/lib/util/InterpolatingTreeMap.java)
 */
class InterpolatingTreeMap : TreeMap<Double, Double>() {

    /**
     * Returns the linearly-interpolated value for [key] or null if it's out of range.
     */
    fun getInterpolated(key: Double): Double? {
        val exactValue = get(key)
        if (exactValue == null) {
            val topBound = ceilingKey(key)
            val bottomBound = floorKey(key)
            if (topBound == null && bottomBound == null) {
                return null
            } else if (topBound == null) {
                return get(bottomBound)
            } else if (bottomBound == null) {
                return get(topBound)
            }

            val topElem = get(topBound)!!
            val bottomElem = get(bottomBound)!!
            return bottomElem + (key - bottomBound) / (topBound - bottomBound) * (topElem - bottomElem)
        }
        return exactValue
    }
}