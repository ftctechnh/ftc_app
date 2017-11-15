package org.firstinspires.ftc.team9853.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.chathamrobotics.common.Controller;
import org.firstinspires.ftc.team9853.Robot9853;

/**
 * Created by carsonstorm on 9/28/2017.
 */

@TeleOp(name = "Basic Drive")
public class BasicDrive extends OpMode {
    private Robot9853 robot;
    private Controller controller1;
    private Servo topLeftLift, topRightLift, bottomLeftLift, bottomRightLift;

    @Override
    public void init() {
        robot = Robot9853.build(this);
        robot.init();

        topLeftLift = hardwareMap.servo.get("TopLeftLift");
        topRightLift = hardwareMap.servo.get("TopRightLift");
        bottomLeftLift = hardwareMap.servo.get("BottomLeftLift");
        bottomRightLift = hardwareMap.servo.get("BottomRightLift");

        bottomRightLift.setPosition(0.6);
        topLeftLift.setPosition(0.6);

        topRightLift.setPosition(0.6);
        bottomLeftLift.setPosition(0.6);

        controller1 = new Controller(gamepad1);
    }

    @Override
    public void loop() {
        controller1.update();
        robot.driveWithControls(gamepad1);

        if (controller1.aState == Controller.ButtonState.TAPPED) {
            bottomRightLift.setPosition(0.6);
            topLeftLift.setPosition(0.6);

            topRightLift.setPosition(0.4);
            bottomLeftLift.setPosition(0.4);
        }

        if (controller1.bState == Controller.ButtonState.TAPPED) {
            bottomRightLift.setPosition(0);
            topLeftLift.setPosition(0);

            topRightLift.setPosition(1);
            bottomLeftLift.setPosition(1);
        }
    }
}
