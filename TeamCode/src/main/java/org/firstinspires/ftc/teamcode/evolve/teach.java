package org.firstinspires.ftc.teamcode.evolve;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Eric on 9/20/2018.
 */

@Disabled
public class teach extends LinearOpMode {

    DcMotor test;

    public void runOpMode (){

        test = hardwareMap.dcMotor.get("test");

        int x = 0;

        waitForStart();

        while (opModeIsActive()){

            if (x == 7){
                telemetry.addData("x", x);
                telemetry.update();
            }

            x = Math.round(gamepad1.right_stick_y * 10);

            test.setPower(x / 10);

        }

    }

}
