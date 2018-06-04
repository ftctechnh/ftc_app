package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import static org.firstinspires.ftc.teamcode.Ftc12547Config.*;


class JewelDestroyer {
    private float hsvValues[] = {0F, 0F, 0F};

    private LinearOpMode autonomousMode;
    private HardwarePushbot robot;
    private Telemetry telemetry;
    private EncoderDriver encoderDriver;
    private ColorSensor sensorColor;

    JewelDestroyer(LinearOpMode autonomousMode, HardwarePushbot robot, EncoderDriver encoderDriver, Telemetry telemetry) {
        this.autonomousMode = autonomousMode;
        this.robot = robot;
        this.encoderDriver = encoderDriver;
        this.telemetry = telemetry;
    }

    public void init() {
        // get a reference to the color sensor.
        sensorColor = autonomousMode.hardwareMap.get(ColorSensor.class, "sensor_color_distance");
    }

    /**
     * @return inches that moved when kicking the jewel
     */
    public double jewelDestroy() {
        double movedInches = 0;

        // (1) Lower the arm controlled by a servo
        lowerJewelMovingArmServo();
        autonomousMode.sleep(1);

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

            movedInches = JEWEL_DISPOSITION_DISTANCE_INCHES;
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

            movedInches = -JEWEL_DISPOSITION_DISTANCE_INCHES;
        }

        robot.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return movedInches;
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
            autonomousMode.sleep(SLEEP_INTERVAL_BETWEEN_SERVO_MOVES_MS);
        }
    }

    private void raiseJewelMovingArmServo() {
        for(double d = JEWEL_ARM_HORIZONTAL_SERVO_POSITION; d < JEWEL_ARM_VERTICAL_SERVO_POSITION;
            d+=JEWEL_ARM_SERVO_MOVING_STEP_CHANGE){
            robot.jewelAnnihilator.setPosition(d);
            autonomousMode.sleep(SLEEP_INTERVAL_BETWEEN_SERVO_MOVES_MS);
        }
    }
}