package org.swerverobotics.library;

/**
 * ITelemetryValue has a single abstract method by which values for telemetry can be (later) computed.
 */
public interface ITelemetryValue
    {
    Object value();
    }
