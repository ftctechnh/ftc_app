package org.firstinspires.ftc.team9374.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team9374.team9374.Hardware9374;

/**
 * Created by darwin on 11/29/16.
 */

//  must be set up such that center of the robot is aligned with the square directly to the left.
@Autonomous(name = "Beta_light sensor")

public class BETA_Light_sensor extends LinearOpMode {

    Hardware9374 robot = new Hardware9374();
    public void runOpMode() throws InterruptedException{
        robot.init(hardwareMap);

        robot.moveToPosition(61,.7);

        robot.Turn(90,1,false);

        robot.moveToPosition(67,.7);

    }
}