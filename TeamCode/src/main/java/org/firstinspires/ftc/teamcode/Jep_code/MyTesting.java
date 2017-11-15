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

    double dtestpwr;
    double dtestpwr2;

    final double DTEST_POSITION_CLOSED = 0;
    final double DTEST_POSITION_OPEN = 1;

    @Override
    public void init() {

        Map map = new Map(hardwareMap,telemetry);
        map.motor("test");

    }

    @Override
    public void loop() {
if (gamepad2.right_bumper){
    dtestpwr = DTEST_POSITION_CLOSED;
}else{
    dtestpwr = DTEST_POSITION_OPEN;
        }
        test.setPower(dtestpwr);
    }
}
