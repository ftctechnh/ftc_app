package org.firstinspires.ftc.team9853.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.team9853.Robot9853;

public class Drive extends LinearOpMode {

    Robot9853 robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = Robot9853.build(this);
        robot.init();
        waitForStart();

        robot.driver.setDrivePower(0,1);
        sleep(1000);
        robot.driver.setDrivePower(0,0);

    }
}
