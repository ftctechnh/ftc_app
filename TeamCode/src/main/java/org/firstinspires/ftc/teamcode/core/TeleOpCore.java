package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;



@SuppressWarnings("WeakerAccess")
@TeleOp(name = "TeleOp", group = "Nessie")

public class TeleOpCore extends OpMode {
    Hardware Hw = new Hardware(telemetry, hardwareMap);

    @Override
    public void init() {
        Hw.initAllHardware(hardwareMap);
        telemetry.addData("Check","1");
        telemetry.update();
    }

    @Override
    public void loop()
    {
        //Forward and Backward for left and right wheels
        Hw.backLeftDrive.setPower(gamepad1.left_stick_y);
        Hw.frontLeftDrive.setPower(gamepad1.left_stick_y);

        Hw.backRightDrive.setPower(gamepad1.right_stick_y);
        Hw.frontRightDrive.setPower(gamepad1.right_stick_y);
        //Turning In-Place
        if(gamepad1.right_trigger > 0){
            Hw.backLeftDrive.setPower(1);
            Hw.backRightDrive.setPower(-1);
            Hw.frontLeftDrive.setPower(1);
            Hw.frontRightDrive.setPower(-1);
        }
        if(gamepad1.left_trigger > 0){
            Hw.backLeftDrive.setPower(1);
            Hw.backRightDrive.setPower(-1);
            Hw.frontLeftDrive.setPower(1);
            Hw.frontRightDrive.setPower(-1);
        }



    }
}
