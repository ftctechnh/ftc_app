package com.acmerobotics.roadrunner.util

/**
 * Clock interface with nanosecond precision and no guarantee about its origin (that is, this is only suited for
 * measuring relative/elapsed time).
 */
interface NanoClock {

    companion object {
        /**
         * Returns a [NanoClock] backed by [System.nanoTime].
         */
        fun system() = object : NanoClock {
            override fun seconds() = System.nanoTime() / 1e9
        }
    }

    /**
     * Returns the number of seconds since an arbitrary (yet consistent) origin.
     */
    fun seconds(): Double
}