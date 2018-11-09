package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Benla on 11/9/2018.
 */

@TeleOp (group = "Voltage", name = "TeleOp") //this is a thing that lets you see the OpMode from the phone.
                                                // Teleops have the @TeleOp annotation
                                                //Autonomous has the @Autonomous annotation
                                                //Classes you don't want to show up on the driver's station, such as base support classes, annotate with @Disabled
public class VoltageTeleOp extends VoltageBase
{
    @Override
    public void DefineOpMode ()
    {
        waitForStart();

        while (opModeIsActive())
        {
            /*
            Put controls here

            You can still use the VoltageBase methods in teleOp.
            Say you had an arm raising method for autonomous.  you can call it with a button on the
            gamepads if you want.

            if (gamepad2.a)
            {
                RaiseArm();
            }

            */


            idle(); //put this at the end of larger while loops to let the software catch up with itself.
        }
    }
}
