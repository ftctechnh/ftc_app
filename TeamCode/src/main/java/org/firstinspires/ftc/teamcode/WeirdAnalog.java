package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by connorespenshade on 9/7/17.
 */

@TeleOp(name = "Weird Analog")
public class WeirdAnalog extends LinearOpMode{

    private DcMotor leftFront;
    private DcMotor leftBack;
    private DcMotor rightFront;
    private DcMotor rightBack;

    @Override
    public void runOpMode() throws InterruptedException {


        initDCMotors();

        waitForStart();

        while(opModeIsActive()) {


            float triggerValue1;
            float triggerValue2;

            float sineFunctionValue;
            float cubeRootValue;

            if (gamepad1.right_trigger >= gamepad1.left_trigger) {
                triggerValue1 = gamepad1.right_trigger;

            } else {
                triggerValue1 = -gamepad1.left_trigger;
            }

            if (gamepad2.right_trigger >= gamepad2.left_trigger) {
                triggerValue2 = gamepad2.right_trigger;
            } else {
                triggerValue2 = -gamepad2.left_trigger;
            }

            sineFunctionValue = sineFunction(triggerValue1);
            cubeRootValue = cubeRoot(triggerValue2);

            leftFront.setPower(sineFunctionValue);
            leftBack.setPower(sineFunctionValue);

            rightFront.setPower(cubeRootValue);
            rightBack.setPower(cubeRootValue);

            idle();
        }

    }

    public void initDCMotors() {

        leftFront = hardwareMap.dcMotor.get("frontLeft");
        leftBack = hardwareMap.dcMotor.get("backLeft");
        rightFront = hardwareMap.dcMotor.get("backRight");
        rightBack = hardwareMap.dcMotor.get("frontRight");

        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    private float sineFunction(float gamepadAnalog) {

        double value;

        value = Math.PI * double.class.cast(gamepadAnalog);

        double sine = Math.sin(value);

        return float.class.cast(sine);

    }

    private float cubeRoot(float gamepadAnalog) {

        double value = double.class.cast(gamepadAnalog);

        value = Math.cbrt(value);

        return float.class.cast(value);

    }

}
