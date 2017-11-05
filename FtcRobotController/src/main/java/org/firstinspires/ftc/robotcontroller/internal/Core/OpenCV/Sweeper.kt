@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.opencv


/**
 * Calls the garbage collector every so often- helps to mitigate memory leaks from OpenCV
 *
 * Constructor- takes interval between forced garbage collection in milliseconds
 */
class Sweeper(private val _interval: Long)
{
    private var _initialTime = System.currentTimeMillis()

    /**
     * Forces garbage collection every so often- time intervals are defined by the constructor
     */
    fun sweep()
    {
        if(System.currentTimeMillis() - _initialTime >= _interval)
        {
            System.gc()
            System.runFinalization()
        }
    }
}