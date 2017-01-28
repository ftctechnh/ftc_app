package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by mikepietrafesa1 on 1/24/17.
 */
@Autonomous
public class test{

    HardwareMap hardwareMap = null;

    DcMotor dcMotor = null;

    DcMotor left1;
    DcMotor left2;
    DcMotor right1;
    DcMotor right2;
    DcMotor lift;
    DcMotor hatch;


    void inittializeMotors() {
        left1 = hardwareMap.dcMotor.get("left1");
        left2 = hardwareMap.dcMotor.get("left2");
        right1 = hardwareMap.dcMotor.get("right1");
        right2 = hardwareMap.dcMotor.get("right2");
        lift = hardwareMap.dcMotor.get("lift");
        hatch = hardwareMap.dcMotor.get("hatch");

    }

    void goForward() {
        right1.setPower(1);
        right2.setPower(1);
        left1.setPower(-1);
        left2.setPower(-1);
    }

    void stop() {
        right1.setPower(0);
        right2.setPower(0);
        left1.setPower(0);
        left2.setPower(0);
    }






}
