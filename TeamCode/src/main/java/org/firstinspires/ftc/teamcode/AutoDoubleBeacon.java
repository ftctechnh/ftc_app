package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Kota Baer on 10/11/2016.
 */

@Autonomous(name = "AutoDoubleBeacon", group = "Concept")
public class AutoDoubleBeacon extends LinearOpMode {
    double Distance = 12; //in inches

    @Override
    public void runOpMode() throws InterruptedException {
        Hardware5035 robot = new Hardware5035();
        robot.init(hardwareMap);
        waitForStart();
        /*
        robot.drive(Distance);
        robot.driveReverse(Distance);
        telemetry.addData("value of drive", Distance);
        telemetry.update();
         */
        while(true) {
            robot.throwingmotor1.setPower(1);
            robot.throwingmotor2.setPower(1);
            idle();
        }
    }
}
