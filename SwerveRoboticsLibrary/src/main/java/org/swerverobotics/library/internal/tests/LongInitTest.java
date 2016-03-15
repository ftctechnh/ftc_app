package org.swerverobotics.library.internal.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

@TeleOp(name="Long Init Test (Loop)", group="Swerve Tests")
@Disabled
public class LongInitTest extends OpMode
    {
    int counter = 0;

    @Override
    public void init()
        {
        try {
            Thread.sleep(3000);
            }
        catch (InterruptedException ignored)
            {
            }
        }

    @Override
    public void loop()
        {
        telemetry.addData("counter", counter++);
        }
    }
