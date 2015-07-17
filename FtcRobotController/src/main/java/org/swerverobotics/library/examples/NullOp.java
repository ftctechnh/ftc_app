package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;

/**
 * Not yet complete!
 */
public class NullOp extends SynchronousOpMode
    {
    @Override protected void main() throws InterruptedException
        {
        this.telemetry.addData("log", "Hello world!");

        for (;;)
            {
            }
        }
    }
