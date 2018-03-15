package org.firstinspires.ftc.teamcode.opmodes.diagnostic;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

/**
 * Created by Noah on 3/6/2018.
 */
/*
@TeleOp(name = "Intake Servo Calibrate", group = "Main")
@Disabled
public class ContServoCalibrate extends OpMode {

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
    boolean lastA = false;
    boolean front = false;
    int incIndex = 0;

    double frontPower = 0;
    double backPower = 0;

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
        if(!lastDPad) {
            if (gamepad1.dpad_up) {
                if(front) frontPower = Range.clip(frontPower + Incs.values()[incIndex].inc, -1, 1);
                else backPower = Range.clip(backPower + Incs.values()[incIndex].inc, -1, 1);
            }
            else if (gamepad1.dpad_down) {
                if(front) frontPower = Range.clip(frontPower - Incs.values()[incIndex].inc, -1, 1);
                else backPower = Range.clip(backPower - Incs.values()[incIndex].inc, -1, 1);
            }
            else if (gamepad1.dpad_left) incIndex = Range.clip(incIndex - 1, 0, Incs.values().length - 1);
            else if (gamepad1.dpad_right) incIndex = Range.clip(incIndex + 1, 0, Incs.values().length - 1);

            if(gamepad1.dpad_up || gamepad1.dpad_down) {
                if(front) {
                    BotHardware.ContiniuosServoE.FrontSuckLeft.servo.setPower(frontPower);
                    BotHardware.ContiniuosServoE.FrontSuckRight.servo.setPower(frontPower);
                }
                else {
                    BotHardware.ContiniuosServoE.SuckLeft.servo.setPower(backPower);
                    BotHardware.ContiniuosServoE.SuckRight.servo.setPower(backPower);
                }
            }
        }

        lastDPad = gamepad1.dpad_up || gamepad1.dpad_right || gamepad1.dpad_left || gamepad1.dpad_down;

        if(!lastA && gamepad1.a) front = !front;
        lastA = gamepad1.a;

        telemetry.addData("Intake", front ? "Front" : "Back");
        telemetry.addData("Servo Front", frontPower);
        telemetry.addData("Servo Back", backPower);
        telemetry.addData("Increment", Incs.values()[incIndex].inc);
    }
}
*/