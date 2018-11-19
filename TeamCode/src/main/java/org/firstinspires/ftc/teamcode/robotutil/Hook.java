package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by antonlin on 9/9/18.
 */

public class Hook {
    private Servo hook;
    LinearOpMode opMode;
    Logger l = new Logger("HOOK");

    private double hookPos = 0.5;
    private double releasePos = 1.0;

    public Hook(LinearOpMode opMode) {
        this.opMode = opMode;
        hook = opMode.hardwareMap.servo.get("hook");
        retract();
    }

    public void attach() {
        l.log("hook attached");
        hook.setPosition(hookPos);
    }

    public void retract() {
        l.log("hook detached");
        hook.setPosition(releasePos);
    }

}
