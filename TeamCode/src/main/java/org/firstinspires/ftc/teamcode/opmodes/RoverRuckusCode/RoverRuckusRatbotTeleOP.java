package org.firstinspires.ftc.teamcode.opmodes.RoverRuckusCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.libraries.TelemetryLogLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.RoverRuckusRatbotHardware;
/**
 * Created by bremm on 10/26/18.
 */

@TeleOp(name="Rover Ruckus TeleOp")

public class RoverRuckusRatbotTeleOP extends OpMode{
    RoverRuckusRatbotHardware robot =  new RoverRuckusRatbotHardware();
    double left, right;

    public void init() {
        robot.init(hardwareMap);
    }

    public void loop() {
        left = (-1)* Math.pow(gamepad1.left_stick_y, 3);
        right = (-1)* Math.pow(gamepad1.right_stick_y, 3);

        robot.flDrive.setPower(left);
        robot.rlDrive.setPower(left);
        robot.frDrive.setPower(right);
        robot.rrDrive.setPower(right);

        telemetry.addData("gamepad_1", gamepad1);
    }
}
