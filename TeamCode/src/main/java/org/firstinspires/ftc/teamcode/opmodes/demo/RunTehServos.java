package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Noah on 3/14/2018.
 */


@Autonomous(name="Run Servos")
@Disabled
public class RunTehServos extends OpMode {
    CRServo left;
    CRServo right;


    public void init() {
        left = hardwareMap.get(CRServo.class, "l");
        right = hardwareMap.get(CRServo.class, "r");
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        left.setPower(0);
        right.setPower(0);
    }

    public void start() {

    }

    public void loop() {
        left.setPower(0.83);
        right.setPower(0.83);
    }

    public void stop() {
        left.setPower(0);
        right.setPower(0);
    }
}
