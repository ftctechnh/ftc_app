package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public abstract class RobotController extends OpMode {


    void startup(){
        /* HARDWARE */

        // Motors
        DcMotor fr = hardwareMap.dcMotor.get("fr");
        DcMotor fl = hardwareMap.dcMotor.get("fl");
        DcMotor br = hardwareMap.dcMotor.get("br");
        DcMotor bl = hardwareMap.dcMotor.get("bl");

        // Servos
        Servo claw = hardwareMap.servo.get("claw");

        // Sensors
        TouchSensor glyph_detect = hardwareMap.touchSensor.get("glyph detect");
    }

    /* METHODS */

    // Driving

    void drive(int distance) {
        // Drive for the distance
    }

}
