package org.firstinspires.ftc.teamcode.opmodes.RoverRuckusCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.libraries.hardware.RoverRuckusRatbotHardware;

import java.text.DecimalFormat;

/**
 * Created by bremm on 10/26/18.
 */

@TeleOp(name="Rover Ruckus TeleOp")



public class RoverRuckusRatbotTeleOP extends OpMode{
    RoverRuckusRatbotHardware robot =  new RoverRuckusRatbotHardware();
    double left, right;
    double speedFactor = 0.5;
    DecimalFormat printFormat = new DecimalFormat ("#.###");
    boolean brake = false;
    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        if(gamepad1.left_bumper && !gamepad1.right_bumper){
            speedFactor = 1; }
        else if(gamepad1.right_bumper && !gamepad1.left_bumper){
            speedFactor = 0.25; }
        else if(!gamepad1.right_bumper && !gamepad1.left_bumper){
            speedFactor = 0.5; }

        while(gamepad1.b) {
            robot.fl.setPower(0);
            robot.bl.setPower(0);
            robot.fr.setPower(0);
            robot.br.setPower(0);
        }

        left = (-1)* Math.pow(gamepad1.left_stick_y, 3) * speedFactor;
        right = (-1)* Math.pow(gamepad1.right_stick_y, 3) * speedFactor;

        robot.fl.setPower(left);
        robot.bl.setPower(left);
        robot.fr.setPower(right);
        robot.br.setPower(right);

        telemetry.addData("Gamepad 1: ", gamepad1);
        telemetry.addData("Speed Factor: ", printFormat.format(speedFactor));
        telemetry.addData("Left: ", printFormat.format(left));
        telemetry.addData("Right: ", printFormat.format(right));

    }
}
