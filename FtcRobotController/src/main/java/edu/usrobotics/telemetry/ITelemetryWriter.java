package edu.usrobotics.telemetry;

/**
 * Created by Max on 9/9/2016.
 */
public interface ITelemetryWriter {

    void init ();
    boolean write (String line);
    void stop ();
}
