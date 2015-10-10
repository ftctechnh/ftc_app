package org.swerverobotics.library.interfaces;

/**
 * IFunc<TResult> represents a nullary function that returns a value of the indicated type. This
 * is conceptually related to Callable, but does not permit the throwing of checked exceptions or
 * (equivalently) require callers to tediously deal with the possibility that a checked exception
 * might be thrown.
 */
public interface IFunc<TResult>
    {
    TResult value();
    }
