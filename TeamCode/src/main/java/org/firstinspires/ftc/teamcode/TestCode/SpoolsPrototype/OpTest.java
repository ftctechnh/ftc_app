package org.firstinspires.ftc.teamcode.TestCode.SpoolsPrototype;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


/**
 * Quick OpMode that just tests the spools
 */
@TeleOp(name = "Spools Test" , group = "Prototypes")
@Disabled
@SuppressWarnings("unused")
public class OpTest extends LinearOpMode
{
    private Base base = new Base();


    public void runOpMode() throws InterruptedException
    {
        base.init(hardwareMap);

        waitForStart();

        while(!isStopRequested())
        {
            if(gamepad1.a)
            {
                base.spools.state = Spools.State.INTAKE;
            }
            else if(gamepad1.b)
            {
                base.spools.state = Spools.State.OUTPUT;
            }
            else
            {
                base.spools.state = Spools.State.STANDBY;
            }
        }
    }
}
