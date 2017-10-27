package org.firstinspires.ftc.teamcode.GeneralCode.PreviousSeasonOpModes.VelocityVortexOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class State_Cap_Only_Autonomous extends LinearOpMode {
    DcMotor fleft;
    DcMotor fright;
    DcMotor bleft;
    DcMotor bright;
    TouchSensor touch;
    public static final float DRIVESPEED = 0.5f;

    public void runOpMode() {
        fleft = hardwareMap.dcMotor.get("fleft");
        fright = hardwareMap.dcMotor.get("fright");
        bleft = hardwareMap.dcMotor.get("bleft");
        bright = hardwareMap.dcMotor.get("bright");
        touch = hardwareMap.touchSensor.get("touch");
        ElapsedTime runtime = new ElapsedTime();
        //Reversing right motors so that they both go the same way
        fright.setDirection(DcMotor.Direction.REVERSE);
        bright.setDirection(DcMotor.Direction.REVERSE);

            waitForStart();

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 10.0)) {

        }
        while(opModeIsActive() && !touch.isPressed()) {
            fright.setPower(DRIVESPEED);
            bright.setPower(DRIVESPEED);
            fleft.setPower(DRIVESPEED);
            bleft.setPower(DRIVESPEED);
        }

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            fright.setPower(DRIVESPEED);
            bright.setPower(DRIVESPEED);
            fleft.setPower(DRIVESPEED);
            bleft.setPower(DRIVESPEED);
        }

        fright.setPower(0);
        bright.setPower(0);
        fleft.setPower(0);
        bleft.setPower(0);
    }
}