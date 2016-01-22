package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.ftcrobotcontroller.hardware.Power;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class BotAuton extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;

    Servo dump;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        //autonomous here
        //full speed = 2.35294 ft/sec
        motorLeft.setPower(Power.FULL_SPEED);
        motorRight.setPower(Power.FULL_SPEED);
        Thread.sleep(4456);
        //100% speed, 10.49 ft forward
        stopMotors();
        motorRight.setPower(Power.NORMAL_SPEED);
        Thread.sleep(300);
        //this is a guess for 90 degree turn to the right
        stopMotors();
        motorRight.setPower(Power.NORMAL_SPEED);
        Thread.sleep(17);
        //75% speed, 36 inches forward
        stopMotors();
        dump.setPosition(Values.DUMP_DOWN);
        Thread.sleep(1000);
        dump.setPosition(Values.DUMP_UP);
        //dump climbers here?
        stopMotors();
    }

    public void initMotors() {
        HardwareManager manager = new HardwareManager(hardwareMap);

        motorRight = manager.getMotor(Values.RIGHT_MOTOR);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = manager.getMotor(Values.LEFT_MOTOR);

        dump = manager.getServo(Values.DUMP);
    }

    public void stopMotors() {
        motorLeft.setPower(Power.FULL_STOP);
        motorRight.setPower(Power.FULL_STOP);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
    }
}
