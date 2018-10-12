package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
/**
 * Created by Eric on 8/29/2017.
 */


@Disabled
@TeleOp
public class BBot_test extends LinearOpMode{

    GyroSensor g;
    DcMotor m1;
    DcMotor m2;
   //
   //
    @Override
    public void runOpMode() throws InterruptedException {
        //
        //
        m1 = hardwareMap.dcMotor.get("motor_1");
        m2 = hardwareMap.dcMotor.get("motor_2");
        m2.setDirection(DcMotor.Direction.REVERSE);
        //
        g = hardwareMap.gyroSensor.get("sensor_gyro");
        //
        waitForStart();
        //
        m1.setPower(.75);
        m2.setPower(.75);
        //
        wait(100);
        //
        m1.setPower(-.75);
        m2.setPower(-.75);
        //
        wait(100);
        m1.setPower(0);
        m2.setPower(0);
        }
    }

