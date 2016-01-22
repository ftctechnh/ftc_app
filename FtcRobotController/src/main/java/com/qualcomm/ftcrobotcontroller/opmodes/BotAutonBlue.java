package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.ftcrobotcontroller.hardware.MotorRunner;
import com.qualcomm.ftcrobotcontroller.hardware.Power;
import com.qualcomm.ftcrobotcontroller.units.EncoderUnit;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class BotAutonBlue extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;

    Servo dump;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        //autonomous here
        //full speed = 2.35294 ft/sec
        MotorRunner.run(new DcMotor[]{motorLeft, motorRight}, Power.FULL_SPEED, new EncoderUnit(319.752f, 10.4f, EncoderUnit.ROTATION_ANDYMARK));
        //100% speed, estimated 10.49 ft forward
        MotorRunner.run(motorLeft, Power.FULL_SPEED, new EncoderUnit(3, 10.4f, EncoderUnit.ROTATION_ANDYMARK));
        //this is a guess for 90 degree turn to the right
        stopMotors();
        MotorRunner.run(new DcMotor[]{motorLeft, motorRight}, Power.FULL_SPEED, new EncoderUnit(91.44f, 10.4f, EncoderUnit.ROTATION_ANDYMARK));
        //100% speed, 3 ft inches forward
        dump.setPosition(Values.DUMP_DOWN);
        Thread.sleep(1000);
        dump.setPosition(Values.DUMP_UP);
        //dump climbers here?
        stopMotors();
    }

    public void initMotors() {
        HardwareManager manager = new HardwareManager(hardwareMap);

        motorRight = manager.getMotor(Values.RIGHT_MOTOR);
        motorLeft = manager.getMotor(Values.LEFT_MOTOR);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        dump = manager.getServo(Values.DUMP);
    }

    public void stopMotors() {
        motorLeft.setPower(Power.FULL_STOP);
        motorRight.setPower(Power.FULL_STOP);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
    }
}
