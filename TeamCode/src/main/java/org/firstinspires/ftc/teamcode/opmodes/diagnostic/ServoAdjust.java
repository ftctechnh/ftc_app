/*
    Main robot teleop program
 */

package org.firstinspires.ftc.teamcode.opmodes.diagnostic;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

@TeleOp(name = "Servo Calibrate", group = "Main")
//@Disabled
public class ServoAdjust extends OpMode {

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
        telemetry.addData("Servo", bot.getStick().getPosition());
        if (gamepad1.dpad_up)
            bot.getStick().setPosition(Range.clip(bot.getStick().getPosition() + 0.01, -1, 1));
        else if (gamepad1.dpad_down)
            bot.getStick().setPosition(Range.clip(bot.getStick().getPosition() - 0.01, -1, 1));
    }
}
