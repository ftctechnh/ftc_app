package org.swerverobotics.library.internal.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
