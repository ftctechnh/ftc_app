package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorController.RunMode;

import java.sql.Driver;

/**
 * Created by Peter on 10/25/2015.
 */
public class TestAutoBot extends OpMode
{

    DriverInterface robot;


            public void init()
            {
               robot = new TestBot(hardwareMap);

            }

            public void start()
            {
                robot.moveStraightEncoders(10, .5f);
            }
            public void loop()
            {

            }

            public void stop()
            {
                stop();
            }
}
