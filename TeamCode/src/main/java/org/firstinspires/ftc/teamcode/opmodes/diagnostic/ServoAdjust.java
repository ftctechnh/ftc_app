/*
    Main robot teleop program
 */

package org.firstinspires.ftc.teamcode.opmodes.diagnostic;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.opmodes.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.opmodes.hardware.BotHardwareOld;

@TeleOp(name = "Servo Calibrate", group = "Main")
//@Disabled
public class ServoAdjust extends OpMode {

    boolean currentServo = false;
    boolean dpadDelay = false;

    BotHardware bot = new BotHardware(this);

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");

        //currentServo = robot.leftServo;

        // hardware maps
        bot.init();
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
            telemetry.addData("Left Servo", bot.getLeftLift().getPosition());
            if (gamepad1.dpad_up)
                bot.getLeftLift().setPosition(Range.clip(bot.getLeftLift().getPosition() + 0.01, -1, 1));
            else if (gamepad1.dpad_down)
                bot.getLeftLift().setPosition(Range.clip(bot.getLeftLift().getPosition() - 0.01, -1, 1));
        } else {
            telemetry.addData("Right Servo", bot.getRightLift().getPosition());
            if (gamepad1.dpad_up)
                bot.getRightLift().setPosition(Range.clip(bot.getRightLift().getPosition() + 0.01, -1, 1));
            else if (gamepad1.dpad_down)
                bot.getRightLift().setPosition(Range.clip(bot.getRightLift().getPosition() - 0.01, -1, 1));
        }

    }
}
