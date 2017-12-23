package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.libraries.FilterLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

/**
 * Created by Noah on 10/27/2017.
 * Teleop!
 */

@TeleOp(name="Teleop with Filter")
@Disabled
public class TeleopWithFilter extends OpMode {
    BotHardware bot = new BotHardware(this);
    FilterLib.MovingWindowFilter leftStick;
    FilterLib.MovingWindowFilter rightStick;

    private int SIZE = 10;

    private boolean pressedUp = false;
    private boolean pressedDown = false;

    public void init() {
        bot.init();
    }

    public void init_loop() {
        if(!pressedUp && gamepad1.dpad_up) SIZE++;
        if(!pressedDown && gamepad1.dpad_down) SIZE--;
        pressedUp = gamepad1.dpad_up;
        pressedDown = gamepad1.dpad_down;
        telemetry.addData("SIZE", SIZE);
    }

    public void start() {
        leftStick = new FilterLib.MovingWindowFilter(SIZE);
        rightStick = new FilterLib.MovingWindowAngleFilter(SIZE);
    }

    public void loop() {
        //leftStick.appendValue(gamepad1.left_stick_y);
        //rightStick.appendValue(gamepad1.right_stick_y);

        bot.setLeftDrive(gamepad1.left_stick_y * 0.5);
        bot.setRightDrive(gamepad1.right_stick_y * 0.5);
    }

    public void stop() {
        bot.stopAll();
    }
}
