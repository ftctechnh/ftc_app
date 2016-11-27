package org.firstinspires.ftc.team9374;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by darwin on 9/20/16.
 */

@TeleOp(name="Basic Tank Drive!", group="Normal_Opmode")

public class Mecanum_Function_ArcadeD extends OpMode {
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    String mode;
    public void init()  {
        mode = "tank";
        leftFront = hardwareMap.dcMotor.get("Motor-left");
        rightFront = hardwareMap.dcMotor.get("Motor-right");
        leftBack = hardwareMap.dcMotor.get("Motor-rear-left");
        rightBack = hardwareMap.dcMotor.get("Motor-rear-right");
    }

    public void loop() {
        float lg = -gamepad1.left_stick_y;
        float rg =  -gamepad1.right_stick_y;

        int mod = 1;

        if (gamepad1.left_bumper) {
            mod = 1;
        } else {
            mod = -1;
        }

        leftFront.setPower(lg);
        leftBack.setPower(lg*mod);
        rightFront.setPower(rg * mod);
        rightBack.setPower(rg);
    }
}
