package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Kota Baer on 10/11/2016.
 */
public class AutoDoubleBeacon extends LinearOpMode {
    double Distance = 18.5;

    @Override
    public void runOpMode() throws InterruptedException {
        Hardware5035 robot   = new Hardware5035();
        robot.init(hardwareMap);

       robot.drive(Distance);

    }
}
