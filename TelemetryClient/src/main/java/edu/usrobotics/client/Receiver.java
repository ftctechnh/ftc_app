package edu.usrobotics.client;

/**
 * Created by Max on 9/17/2016.
 */
public interface Receiver {

    boolean hasNext ();

    String getNext ();
}
