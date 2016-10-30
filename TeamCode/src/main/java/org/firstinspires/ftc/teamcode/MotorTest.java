package org.firstinspires.ftc.teamcode;

import android.location.LocationListener;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.LinkedList;

/**
 * Created by Michael on 10/14/2016.
 *
 * */
@TeleOp(name="MotorTest")
public class MotorTest extends RobotHardware {

    @Override public void loop() {

        boolean toggle = gamepad1.x;

            if(toggle) {
                scooperMotor.setPower(0.25);
                toggle = !toggle;
            }

    }

}