package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveEngine {
    DcMotor back;
    DcMotor right;
    DcMotor left;

    public DriveEngine(HardwareMap hardwareMap) {
        back  = hardwareMap.dcMotor.get("back");
        right = hardwareMap.dcMotor.get("right");
        left  = hardwareMap.dcMotor.get("left");

//        back.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        left.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        back.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        back.setDirection (DcMotor.Direction.FORWARD);
        right.setDirection(DcMotor.Direction.FORWARD);
        left.setDirection (DcMotor.Direction.FORWARD);

    }

    public void drive(double x, double y) {
        //back.setPower(Math.cos(0) * x + Math.cos(0) * y);
        //right.setPower(Math.cos(Math.PI * 2 / 3) * x + Math.cos(Math.PI * 2 / 3) * y);
        //left.setPower(Math.cos(Math.PI * 4 / 3) * x + Math.cos(Math.PI * 4 / 3) * y);

        back.setPower(x);
        right.setPower( (-x/2) + ( (y*Math.sqrt(3)) / 2 ) );
        left.setPower ( (-x/2) + ( (-y*Math.sqrt(3)) / 2 ) );
    }

    public void rotate(double y) {
        back.setPower(y);
        right.setPower(y);
        left.setPower(y);

    }
}