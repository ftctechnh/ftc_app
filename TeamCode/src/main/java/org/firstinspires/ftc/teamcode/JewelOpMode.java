package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;


//Jose Sulaiman

@Autonomous
//@Disabled
public class JewelOpMode extends LinearOpMode {

    // Declare OpMode members.

    private Servo arm;
    private DcMotor m1, m2;
    private ColorSensor c_sensor;

    private int initial_angle = 90;
    private int final_angle = 180;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        m1 = hardwareMap.get(DcMotor.class, "Motor1");
        m2 = hardwareMap.get(DcMotor.class, "Motor2");

        m2.setDirection((DcMotor.Direction.REVERSE));
        arm = hardwareMap.get(Servo.class, "Servo1");

        c_sensor = hardwareMap.get(ColorSensor.class, "Color_Sensor");

        double rightPower = 0;
        double leftPower = 0;
        double initial_angle_value = Range.clip(initial_angle, 0, 1);
        double final_angle_value = Range.clip(final_angle, 0, 1);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            arm.setPosition(final_angle_value);
            sleep(100);


            if (c_sensor.alpha() >= 2) {
                if ((c_sensor.blue() >= 2) && (c_sensor.red() <= 5) && (c_sensor.green() <= 5)) {
                    telemetry.addData("Ball Color", "Blue");
                    telemetry.update();
                    rightPower = 1;
                    leftPower = -1;

                } else if ((c_sensor.blue() <= 5) && (c_sensor.red() >= 2) && (c_sensor.green() <= 5)) {
                    telemetry.addData("Ball Color", "Red");
                    telemetry.update();
                    rightPower = -1;
                    leftPower = 1;
                }

                m1.setPower(rightPower);
                m2.setPower(leftPower);

                sleep(100);

                rightPower = 0;
                leftPower = 0;

                m1.setPower(rightPower);
                m2.setPower(leftPower);

                telemetry.update();
            }
        }
    }
}