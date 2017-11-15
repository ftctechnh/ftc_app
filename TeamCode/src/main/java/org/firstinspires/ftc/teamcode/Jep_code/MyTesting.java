package org.firstinspires.ftc.teamcode.Jep_code;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Utilities.Map;

/**
 * Created by jeppe on 17-10-2017.
 */

public class MyTesting extends OpMode{

    DcMotor test;
    DcMotor test2;

    double dtest;
    double dtest2;

    @Override
    public void init() {

        Map map = new Map(hardwareMap,telemetry);
        map.motor("test");
        // test = hardwareMap.dcMotor.get("test");
        test2 = hardwareMap.dcMotor.get("test2");

        test.setDirection(DcMotorSimple.Direction.FORWARD);
        test2.setDirection(DcMotorSimple.Direction.FORWARD);



    }

    @Override
    public void loop() {

        dtest = -gamepad1.right_stick_y;
        dtest2 = -gamepad1.left_stick_y;

    }


}
