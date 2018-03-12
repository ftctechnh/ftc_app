package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

/**
 * Created by Noah on 2/23/2018.
 */

@TeleOp(name="Lift Fixer")
//bwahahaha were going to worlds
public class LiftFix extends OpMode {
    BotHardware bot = new BotHardware(this);

    public void init() {
        bot.init();
        DcMotor[] ray = bot.getMotorRay();
        for(DcMotor motor : ray)
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void start() {

    }

    public void loop() {
        BotHardware.Motor.liftLeft.motor.setPower(gamepad1.left_stick_y / 2.0);
        BotHardware.Motor.liftRight.motor.setPower(gamepad1.right_stick_y / 2.0);
    }

    public void stop() {
        bot.stopAll();
    }
}
