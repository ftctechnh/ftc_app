package org.firstinspires.ftc.team9374.team9374;

/**
 * Created by darwin on 11/30/16.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by darwin on 9/20/16.
 */

@TeleOp(name="Mecahnim_DriveV2", group="Normal_Opmode")

public class Mecanum_Function_ArcadeD extends OpMode {
    DcMotor left_f;
    DcMotor right_f;
    DcMotor left_b;
    DcMotor right_b;
    String mode;
    //Controller vaibles
    double lStickY;
    double lStickX;
    double rStickY;
    double rStickX;
    //Power varibles
    double LFpower;
    double RFpower;
    double LBpower;
    double RBpower;

    final double correction = 1;
    public void init()  {
        mode = "tank";
        left_f = hardwareMap.dcMotor.get("Eng1-left");
        right_f = hardwareMap.dcMotor.get("Eng1-right");
        left_b = hardwareMap.dcMotor.get("Eng2-left");
        right_b = hardwareMap.dcMotor.get("Eng2-right");

        left_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        right_b.setDirection(DcMotorSimple.Direction.REVERSE);
        right_f.setDirection(DcMotorSimple.Direction.REVERSE);

        //right_f.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void loop() {
        //Cannot reverse the motors, that will result in broken other controlls
        lStickY = gamepad1.left_stick_y * -1;
        lStickX = gamepad1.left_stick_x * -1;
        rStickX = gamepad1.right_stick_x;


        //Shooter code

        LFpower = lStickY + rStickX + lStickX;
        LBpower = lStickY + rStickX - lStickX;
        RFpower = lStickY - rStickX - lStickX;
        RBpower = lStickY - rStickX + lStickX;
        //------------------------------------

        left_f.setPower(Range.clip(LFpower,-1,1));
        right_f.setPower(Range.clip(RFpower,-1,1));
        left_b.setPower(Range.clip(LBpower,-1,1));
        right_b.setPower(Range.clip(RBpower,-1,1));

        telemetry.addData("Left Back P",left_b.getPower());
        telemetry.addData("Left Front P",left_f.getPower());
        telemetry.addData("Right Back P",right_b.getPower());
        telemetry.addData("Right Front P",right_f.getPower());


    }
}
