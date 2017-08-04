package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by connorespenshade on 8/4/17.
 */

@TeleOp(name = "Tutorial for Burgy")
public class TutorialTeleOpNumber1 extends LinearOpMode {


    private DcMotor motorLeft;
    private DcMotor motorRight;

    private Servo servoMotor;
    private ColorSensor color;

    private double maxPosition = 0.8;
    private double minPosition = 0.2;

    @Override
    public void runOpMode() throws InterruptedException {

        getHardware();

        waitForStart();

        while (opModeIsActive()) {

            motorLeft.setPower(gamepad1.left_stick_y);
            motorRight.setPower(gamepad1.right_stick_y);

            telemetry.addData("Left power", motorLeft.getPower());
            telemetry.addData("Right power", motorRight.getPower());

            if (gamepad1.a) {
                servoMotor.setPosition(maxPosition);
            } else if (gamepad2.b) {
                servoMotor.setPosition(minPosition);
            }
        }
    }

    public void getHardware() {

        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        servoMotor = hardwareMap.servo.get("servo");
        //color = hardwareMap.colorSensor.get("colorsensor")

    }
}


