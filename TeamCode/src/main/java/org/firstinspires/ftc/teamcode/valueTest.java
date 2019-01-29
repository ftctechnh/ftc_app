
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math.*;

/**
 * Created by Wake Robotics Member on 11/2/2017.
 */


@TeleOp(name = "valueTest", group = "Tank")
public class valueTest extends OpMode {

    DcMotor back_right;
    DcMotor back_left;
    DcMotor front_left;
    DcMotor front_right;
    DcMotor plow;

    @Override
    public void init() {
        back_right = hardwareMap.dcMotor.get("back_right");
        back_left = hardwareMap.dcMotor.get("back_left");
        front_left = hardwareMap.dcMotor.get("front_left");
        front_right = hardwareMap.dcMotor.get("front_right");
        plow = hardwareMap.dcMotor.get("plow");
    }

    @Override
    public void loop() {
        telemetry.addData("xPower", "%.2f",  gamepad1.left_stick_x);
        telemetry.addData("yPower", "%.2f",  gamepad1.left_stick_y);

    }
}
