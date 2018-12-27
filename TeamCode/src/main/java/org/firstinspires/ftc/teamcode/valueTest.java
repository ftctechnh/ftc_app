
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

    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    DcMotor m4;
    DcMotor plow;

    @Override
    public void init() {
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        m4 = hardwareMap.dcMotor.get("m4");
        plow = hardwareMap.dcMotor.get("plow");
    }

    @Override
    public void loop() {
        telemetry.addData("xPower", "%.2f",  gamepad1.left_stick_x);
        telemetry.addData("yPower", "%.2f",  gamepad1.left_stick_y);

    }
}
