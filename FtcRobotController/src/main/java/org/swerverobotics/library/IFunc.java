package org.swerverobotics.library;

/**
 * IFunc<TResult> represents a nullary function that returns a value of the indicated type
 */
public interface IFunc<TResult>
    {
    TResult value();
    }
