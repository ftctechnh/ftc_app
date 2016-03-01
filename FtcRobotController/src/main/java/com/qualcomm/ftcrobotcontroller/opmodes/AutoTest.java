package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by wolfie on 2/7/16.
 */

public class AutoTest extends LinearOpMode {

    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor spinners;

    private Servo belt;
    private Servo hook;

    private GyroSensor sensorGyro;

    public void initialize() {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        spinners = hardwareMap.dcMotor.get("spinners");
        sensorGyro.calibrate();
    }

    public void runOpMode() throws InterruptedException {
        initialize();
        EncoderMotorTask motorTaskLeft = new EncoderMotorTask( this, motorLeft);
        EncoderMotorTask motorTaskRight = new EncoderMotorTask(this,motorRight);
        GyroMotorClass gyroTurn = new GyroMotorClass(this,sensorGyro,motorLeft,motorRight);
        while (sensorGyro.isCalibrating())  {
            Thread.sleep(50);
        }
        waitForStart();
        int step = 1;
        while (opModeIsActive()) {

            switch (step) {
                case 1:
                    if (! motorTaskLeft.isRunning()) {
                        //full setPower , forward for 8880
                        motorTaskLeft.startMotor("step1 left", 1, 8712 , Direction.MOTOR_FORWARD);
                    }
                    if (!motorTaskRight.isRunning()) {
                        motorTaskRight.startMotor("step1 right",1,8712,Direction.MOTOR_FORWARD);
                    }
                    if (motorTaskLeft.targetReached()) {
                        motorTaskLeft.stop();
                    }
                    if (motorTaskRight.targetReached()) {
                        motorTaskRight.stop();
                    }
                    if(motorTaskLeft.targetReached() && motorTaskRight.targetReached()) {
                        step++;
                    }
                    break;

                case 2:
                    if (!gyroTurn.isRunning()) {
                        gyroTurn.startPivotTurn(45,-0.55,Direction.MOTOR_RIGHT);
                    }
                    if (gyroTurn.targetReached()) {
                        gyroTurn.stop();
                        step++;
                    }
                    break;
                case 3:
                    if (! motorTaskLeft.isRunning()) {
                        //full setPower , forward for 8880
                        motorTaskLeft.startMotor("step1 left", 1, 2865 , Direction.MOTOR_FORWARD);
                    }
                    if (!motorTaskRight.isRunning()) {
                        motorTaskRight.startMotor("step1 right",1,2865,Direction.MOTOR_FORWARD);
                    }
                    if (motorTaskLeft.targetReached()) {
                        motorTaskLeft.stop();
                    }
                    if (motorTaskRight.targetReached()) {
                        motorTaskRight.stop();
                    }
                    if(motorTaskLeft.targetReached() && motorTaskRight.targetReached()) {
                        step++;
                    }
                    break;
                default:
                    telemetry.addData("step" + step + " Opmode Status", "Tasks completed");
                    break;
            }
            waitOneFullHardwareCycle();
        }
    }
}

