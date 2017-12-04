package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import static org.firstinspires.ftc.teamcode.Ftc12547Config.*;


class JewelDestroyer {

    private LinearOpMode autonomousMode;
    private Telemetry telemetry;

    /* Declare OpMode members. */
    private HardwarePushbot robot   = new HardwarePushbot();   // Use a Pushbot's hardware

    private EncoderDriver encoderDriver = new EncoderDriver(robot, telemetry);

    private ColorSensor sensorColor;
    private DistanceSensor sensorDistance;
    private float hsvValues[] = {0F, 0F, 0F};


    public void JewelDestroy() {
        // (1) Lower the arm controlled by a servo
        lowerJewelMovingArmServo();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                (int) (sensorColor.green() * SCALE_FACTOR),
                (int) (sensorColor.blue() * SCALE_FACTOR),
                hsvValues);

        int jewelColor = (sensorColor.red() > sensorColor.blue()) ? Color.RED : Color.BLUE;
        telemetry.addData("Blue ", sensorColor.blue());
        telemetry.addData("Red ", sensorColor.red());
        telemetry.addData("Jewel color: ", (jewelColor == Color.RED) ? "Red" : "Blue");
        telemetry.update();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        robot.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (jewelColor == TEAM_COLOR) {
            /**
             * (2) Move forward, because
             * a. Color sensor of team 12547 is mounted facing backward.
             * b. Team color jewel is on the back side in this condition.
             */
            MoveForwardForJewelDisposition();

            // (3) Raise the servo that control the arm to move the JewelMovingArm
            raiseJewelMovingArmServo();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            encoderDriver.encoderDrive(ENCODER_RUN_SPEED, -JEWEL_DISPOSITION_DISTANCE_INCHES, -JEWEL_DISPOSITION_DISTANCE_INCHES, 5);
        } else {
            // (2) Move backward if the jewel is not in the team color
            MoveBackwardForJewelDisposition();

            //  (3) Raise the servo that control the arm to move the JewelMovingArm
            raiseJewelMovingArmServo();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            encoderDriver.encoderDrive(ENCODER_RUN_SPEED, JEWEL_DISPOSITION_DISTANCE_INCHES, JEWEL_DISPOSITION_DISTANCE_INCHES, 5);
        }
    robot.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    robot.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }
    private void MoveBackwardForJewelDisposition() {
        encoderDriver.encoderDrive(ENCODER_RUN_SPEED,
                -JEWEL_DISPOSITION_DISTANCE_INCHES,
                -JEWEL_DISPOSITION_DISTANCE_INCHES,
                JEWEL_DISPOSITION_TIMEOUT_SECONDS);
        stopMotorsAndRestShortly();
    }

    private void MoveForwardForJewelDisposition() {
        encoderDriver.encoderDrive(ENCODER_RUN_SPEED,
                JEWEL_DISPOSITION_DISTANCE_INCHES,
                JEWEL_DISPOSITION_DISTANCE_INCHES,
                JEWEL_DISPOSITION_TIMEOUT_SECONDS);
        stopMotorsAndRestShortly();
    }
    private void stopMotorsAndRestShortly() {
        robot.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void lowerJewelMovingArmServo() {
        for(double d = JEWEL_ARM_VERTICAL_SERVO_POSITION; d > JEWEL_ARM_HORIZONTAL_SERVO_POSITION;
            d-=JEWEL_ARM_SERVO_MOVING_STEP_CHANGE){
            robot.jewelAnnihilator.setPosition(d);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void raiseJewelMovingArmServo() {
        for(double d = JEWEL_ARM_HORIZONTAL_SERVO_POSITION; d < JEWEL_ARM_VERTICAL_SERVO_POSITION;
            d+=JEWEL_ARM_SERVO_MOVING_STEP_CHANGE){
            robot.jewelAnnihilator.setPosition(d);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}