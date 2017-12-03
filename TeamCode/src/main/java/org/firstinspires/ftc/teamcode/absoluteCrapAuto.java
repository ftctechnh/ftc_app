package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by jxfio on 12/2/2017.
 */

@Autonomous(name="Crap", group="robot2")
public class absoluteCrapAuto extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor Left;
    private DcMotor Right;
    @Override
    public void runOpMode() {

        Left = hardwareMap.get(DcMotor.class, "left");
        Right = hardwareMap.get(DcMotor.class, "right");

        waitForStart();
        // run until the end of the match (driver presses STOP)

        while (opModeIsActive()) {
            if(getRuntime()<1000){
                Left.setPower(.5);
                Right.setPower(-.5);
            }
            else {
                Left.setPower(0);
                Right.setPower(0);
            }
        }
    }
}
