package org.firstinspires.ftc.team6417;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxEmbeddedIMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="Wafflywaffle", group="Autonomous")
public class Waffle {

    Hardware6417 robot = new Hardware6417();
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    Boolean exit = false;

    public void runOpMode(){



    }

}
