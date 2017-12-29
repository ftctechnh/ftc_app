package org.firstinspires.ftc.teamcode.opmodes.diagnostic;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.hardware.WhackyDistLib;

/**
 * Created by Noah on 12/28/2017.
 */

@TeleOp(name = "Stick Degrees Test", group = "Main")
public class ServoDegreeTest extends OpMode {

    private enum Incs {
        SMALL(1),
        MEDIUM(5),
        EHH(10),
        HOLYMOTHER(50);

        public double inc;
        Incs(double inc) {
            this.inc = inc;
        }
    }

    BotHardware bot = new BotHardware(this);
    boolean lastDPad = false;
    int incIndex = 0;

    double currentDegrees = 90;

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
        telemetry.addData("Degrees", currentDegrees);
        telemetry.addData("Distance", WhackyDistLib.getWhackyDistance(currentDegrees, DistanceUnit.INCH));
        telemetry.addData("Increment", Incs.values()[incIndex].inc);
        if(!lastDPad) {
            if (gamepad1.dpad_up)
                bot.getStick().setPosition(Range.clip(WhackyDistLib.getWhackyPosFromDegrees(currentDegrees += Incs.values()[incIndex].inc), -1, 1));
            else if (gamepad1.dpad_down)
                bot.getStick().setPosition(Range.clip(WhackyDistLib.getWhackyPosFromDegrees(currentDegrees -= Incs.values()[incIndex].inc), -1, 1));
            else if (gamepad1.dpad_left) incIndex = Range.clip(incIndex - 1, 0, Incs.values().length - 1);
            else if (gamepad1.dpad_right) incIndex = Range.clip(incIndex + 1, 0, Incs.values().length - 1);
        }

        lastDPad = gamepad1.dpad_up || gamepad1.dpad_right || gamepad1.dpad_left || gamepad1.dpad_down;
    }

}
