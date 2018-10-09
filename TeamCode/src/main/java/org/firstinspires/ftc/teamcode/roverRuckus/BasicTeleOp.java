package org.firstinspires.ftc.teamcode.roverRuckus;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp (name = "BasicTeleOp", group = "usable")
public class BasicTeleOp extends LinearOpMode {
    //
    DcMotor left;
    DcMotor right;
    //
    public void runOpMode(){
        //
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        //
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        //
        waitForStartify();
        //
        while (opModeIsActive()){
            left.setPower(-gamepad1.left_stick_y);
            right.setPower(-gamepad1.right_stick_y);
        }
    }
    //
    public void waitForStartify(){
        waitForStart();
    }
}
