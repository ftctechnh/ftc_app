package org.firstinspires.ftc.teamcode.opmodes.RoverRuckusCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.libraries.hardware.RoverRuckusRatbotHardware;
/**
 * Created by bremm on 10/26/18.
 */

@TeleOp(name="Rover Ruckus TeleOp")

public class RoverRuckusRatbotTeleOP extends OpMode{
    RoverRuckusRatbotHardware robot =  new RoverRuckusRatbotHardware();
    double left, right;
    double speedFactor = 0.5;

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

        left = (-1)* Math.pow(gamepad1.left_stick_y, 3) * speedFactor;
        right = (-1)* Math.pow(gamepad1.right_stick_y, 3) * speedFactor;

        robot.fl.setPower(left);
        robot.bl.setPower(left);
        robot.fr.setPower(right);
        robot.br.setPower(right);

        telemetry.addData("gamepad_1", gamepad1);
        telemetry.addData("Speed Factor", speedFactor);
        telemetry.addData("Left", left);
        telemetry.addData("Right", right);
    }
}
