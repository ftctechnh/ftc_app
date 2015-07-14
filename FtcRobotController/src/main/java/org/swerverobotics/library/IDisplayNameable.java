package org.swerverobotics.library;

/**
 * Interface to things which have a name by which they can be identified to a user.
 */
public interface IDisplayNameable
    {
    /**
     * A name for human (not programmatic) consumption
     */
    String displayName();
    void setDisplayName(String displayName);
    }
