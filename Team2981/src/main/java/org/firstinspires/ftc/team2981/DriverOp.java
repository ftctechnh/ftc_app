package org.firstinspires.ftc.team2981;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team2981.structural.*;

/**
 * Created by 200462069 on 10/14/2017.
 */

@TeleOp(name = "TeleOp", group="Regular")
public class DriverOp extends OpMode {
    RobotHardware robot;
    Sensors sensors;
    Vision vision;

    @Override
    public void init() {
        robot = new RobotHardware(hardwareMap);
        sensors = new Sensors(hardwareMap);
        vision = new Vision(hardwareMap);

        robot.init();
        sensors.calibrate();
        vision.start();
    }

    @Override
    public void loop() {
        telemetry.addData("VuMark", "%s visible", vision.track());
    }

    @Override
    public void stop(){
        vision.stop();
    }

    public void driverOne(){
        double leftStick = gamepad1.left_stick_y;
        double rightStick = gamepad1.right_stick_y;
        leftStick = (Math.abs(leftStick) > 0.05 ? leftStick : 0);
        rightStick = (Math.abs(rightStick) > 0.05 ? rightStick : 0);

        robot.driveLeft(leftStick);
        robot.driveRight(rightStick);
    }

    public void driverTwo(){
        double power = (gamepad2.dpad_up ? 0.5 : (gamepad2.dpad_down ? -0.5 : 0));
        robot.fourBar.setPower(power);

        if(gamepad2.a) robot.claw.setPosition(robot.claw_closed);
        else if(gamepad2.b) robot.claw.setPosition(robot.claw_open);
    }

    public void telemetry(){

    }


}
