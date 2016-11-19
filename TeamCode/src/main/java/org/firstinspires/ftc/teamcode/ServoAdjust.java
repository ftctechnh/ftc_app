/*
    Main robot teleop program
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.Util;

@TeleOp(name = "Servo Calibrate", group = "Main")
//@Disabled
public class ServoAdjust extends OpMode {

    boolean currentServo = false;
    boolean dpadDelay = false;

    BotHardware robot = new BotHardware();

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");

        //currentServo = robot.leftServo;

        // hardware maps
        robot.init(this, false);
    }

    @Override
    public void start() {}

    @Override
    public void loop() {

        if (gamepad1.dpad_left || gamepad1.dpad_right) {
            if (!dpadDelay) currentServo = !currentServo;
            dpadDelay = true;
        } else dpadDelay = false;


        if (currentServo) {
            telemetry.addData("Left Servo", robot.leftServo.getPosition());
            if (gamepad1.dpad_up)
                robot.leftServo.setPosition(Range.clip(robot.leftServo.getPosition() + 0.01, -1, 1));
            else if (gamepad1.dpad_down)
                robot.leftServo.setPosition(Range.clip(robot.leftServo.getPosition() - 0.01, -1, 1));
        } else {
            telemetry.addData("Right Servo", robot.rightServo.getPosition());
            if (gamepad1.dpad_up)
                robot.rightServo.setPosition(Range.clip(robot.rightServo.getPosition() + 0.01, -1, 1));
            else if (gamepad1.dpad_down)
                robot.rightServo.setPosition(Range.clip(robot.rightServo.getPosition() - 0.01, -1, 1));
        }

    }
}
