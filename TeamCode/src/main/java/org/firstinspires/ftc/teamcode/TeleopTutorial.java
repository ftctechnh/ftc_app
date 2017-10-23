package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Aljohn-1999 on 10/22/2017.
 */

@TeleOp(name = "teleoptutorial", group = "Tutorials")
public class TeleopTutorial extends LinearOpMode
{

    private DcMotor motorLeft;
    private DcMotor motorRight;

    @Override
    public void runOpmode() throws InterruptedException
        {motorLeft = hardwareMap.dcMotor.get("motorLeft") ;
        {motorRight = hardwareMap.dcMotor.get("motorRight") ;

            motorLeft.setDirection(DcMotor.Direction.REVERSE);


            waitForStart();

        while (opModeIsActive())
        {
            idle();
        }
    }
}
