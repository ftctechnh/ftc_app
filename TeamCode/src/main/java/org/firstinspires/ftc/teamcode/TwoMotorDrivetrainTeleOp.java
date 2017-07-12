package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by connorespenshade on 7/12/17.
 */

//Line 11 allows Op Mode to appear on Android device
@TeleOp(name = "Two Motor Drive Train")
public class TwoMotorDrivetrainTeleOp extends LinearOpMode {


    //Create motor variables
    private DcMotor motorLeft;
    private DcMotor motorRight;

    private Servo servoMotor;

    private Double servoMaxPos = 0.8;
    private Double servoMinPos = 0.2;

    @Override
    public void runOpMode() throws InterruptedException {

        //Do stuff to initialize robot

        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");

        motorRight.setDirection(DcMotorSimple.Direction.REVERSE);

        servoMotor = hardwareMap.servo.get("servo");

        waitForStart();

        //Do stuff when started

        while(opModeIsActive()) {

            motorLeft.setPower(-gamepad1.left_stick_y);

            motorRight.setPower(-gamepad1.right_stick_y);

            if (gamepad2.a) {
                servoMotor.setPosition(servoMaxPos);
            } else if (gamepad2.b) {
                servoMotor.setPosition(servoMinPos);
            }

            idle(); //Allows computer to rest for a moment

        }

    }



}
