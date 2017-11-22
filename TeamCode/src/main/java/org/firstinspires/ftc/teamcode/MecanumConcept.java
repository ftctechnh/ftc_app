package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Kaden on 10/20/2017.
 */

@TeleOp(name = "MecanumDriveConcept", group = "linear OpMode")
public class MecanumConcept extends OpMode {
    private DriveMecanum drive;


    @Override
    public void init() {
        drive = new DriveMecanum(
            hardwareMap.dcMotor.get("m1"), //FrontLeft
            hardwareMap.dcMotor.get("m2"), //FrontRight
            hardwareMap.dcMotor.get("m3"), //RearLeft
            hardwareMap.dcMotor.get("m4"), 1.0); //RearRight

    }

    @Override
    public void loop() {
        drive.driveLeftRight(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        if (gamepad1.right_bumper) {
            drive.swingRight();
        }
        else if (gamepad1.left_bumper) {
            drive.swingLeft();
        }
        

    }

    public void reverseMotor(DcMotor motor) {
        motor.setDirection(DcMotor.Direction.REVERSE);
    }

    public double clip(double value) {
        return Range.clip(value, -1,1);
    }

}
