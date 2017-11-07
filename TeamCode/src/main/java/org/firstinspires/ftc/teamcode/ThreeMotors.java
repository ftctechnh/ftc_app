package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.modules.GamepadV2;

/**
 * Created by Kit Caldwell on 10/24/2017.
 */
@TeleOp
public class ThreeMotors extends OpMode {
    DcMotor motorOne;
    DcMotor motorTwo;
    DcMotor motorTres;



    @Override
    public void init() {
        motorOne = hardwareMap.dcMotor.get("one");
        motorTwo = hardwareMap.dcMotor.get("two");
        motorTres = hardwareMap.dcMotor.get("three");

    }

    @Override
    public void loop() {
        motorOne.setPower(gamepad1.left_stick_y);

        motorTwo.setPower(gamepad1.right_stick_y);

        if (gamepad1.a == true){
            motorTres.setPower(.25);
        }
        else if (gamepad1.a == false){
            motorTres.setPower(0);
        }
        if (gamepad1.b == true){
            motorTres.setPower(-.25);
        }
        else if (gamepad1.b == false){
            motorTres.setPower(0);
        }

        telemetry.addData("right stick",gamepad1.right_stick_y);
        telemetry.addData("left stick",gamepad1.left_stick_y);
        telemetry.addData("right bumper",gamepad1.right_bumper);
        telemetry.addData("left bumper",gamepad1.left_bumper);

    }
}
