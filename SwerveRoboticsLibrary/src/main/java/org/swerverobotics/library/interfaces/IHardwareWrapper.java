package org.swerverobotics.library.interfaces;

/**
 * Advanced: IHardwareWrapper can be used on hardware objects which wrap other, similar
 * hardware objects in order to retrieve the object that they wrapp
 */
public interface IHardwareWrapper<T>
    {
    T getWrappedTarget();
    }
