package org.swerverobotics.library.examples;

import org.swerverobotics.library.*;

/**
 * Not yet complete!
 */
public class NullOp extends SynchronousOpMode
    {
    @Override protected void main() throws InterruptedException
        {
        this.telemetry.addData("log", "Hello world!");

        while (!this.stopRequested())
            {
            this.idle();
            }
        }
    }
