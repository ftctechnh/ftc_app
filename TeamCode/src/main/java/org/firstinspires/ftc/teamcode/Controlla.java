package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Swagster_Wagster on 9/29/17.
 */


@TeleOp(name = "Controlla")
public class Controlla extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException{



        waitForStart();

        while (opModeIsActive()){

            idle();
        }
    }
}
