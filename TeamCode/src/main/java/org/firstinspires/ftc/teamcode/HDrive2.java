
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;




@TeleOp(name = "HDrive2", group = "Tank")
public class HDrive2 extends OpMode {

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor middle;


    @Override
    public void init() {
        backRight = hardwareMap.dcMotor.get("back_right");
        backLeft = hardwareMap.dcMotor.get("back_left");
        frontRight = hardwareMap.dcMotor.get("front_right");
        frontLeft = hardwareMap.dcMotor.get("front_left");
        middle = hardwareMap.dcMotor.get("middle");

        //backLeft.setDirection(DcMotor.Direction.REVERSE);
        //frontLeft.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {
        frontLeft.setPower(-gamepad1.left_stick_y / 2.5);
        backLeft.setPower(-gamepad1.left_stick_y / 2.5);
        frontRight.setPower(-gamepad1.right_stick_y / 2.5);
        backRight.setPower(-gamepad1.right_stick_y / 2.5);
        middle.setPower(-gamepad1.right_stick_x / 2.5);

    }

}
