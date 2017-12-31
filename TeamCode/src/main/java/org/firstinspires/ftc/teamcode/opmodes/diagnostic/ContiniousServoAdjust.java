/*
    Main robot teleop program
 */

package org.firstinspires.ftc.teamcode.opmodes.diagnostic;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

@TeleOp(name = "Cont Servo Calibrate", group = "Main")
//@Disabled
public class ContiniousServoAdjust extends OpMode {

    private enum Incs {
        SMALL(0.01),
        MEDIUM(0.05),
        EHH(0.1),
        HOLYMOTHER(0.5);

        public double inc;
        Incs(double inc) {
            this.inc = inc;
        }
    }

    BotHardware bot = new BotHardware(this);
    boolean lastDPad = false;
    int incIndex = 0;

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
        telemetry.addData("Servo", BotHardware.ContiniuosServoE.FrontSuckLeft.servo.getPower());
        telemetry.addData("Increment", Incs.values()[incIndex].inc);
        if(!lastDPad) {
            if (gamepad1.dpad_up)
                BotHardware.ContiniuosServoE.FrontSuckLeft.servo.setPower(Range.clip(BotHardware.ContiniuosServoE.FrontSuckLeft.servo.getPower() + Incs.values()[incIndex].inc, -1, 1));
            else if (gamepad1.dpad_down)
                BotHardware.ContiniuosServoE.FrontSuckLeft.servo.setPower(Range.clip(BotHardware.ContiniuosServoE.FrontSuckLeft.servo.getPower() - Incs.values()[incIndex].inc, -1, 1));
            else if (gamepad1.dpad_left) incIndex = Range.clip(incIndex - 1, 0, Incs.values().length - 1);
            else if (gamepad1.dpad_right) incIndex = Range.clip(incIndex + 1, 0, Incs.values().length - 1);
        }

        lastDPad = gamepad1.dpad_up || gamepad1.dpad_right || gamepad1.dpad_left || gamepad1.dpad_down;
    }
}
