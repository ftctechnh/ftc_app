package org.firstinspires.ftc.teamcode.evolve;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.HardwareSensorMap;

//Created by Eric on 6/10/2018.

@TeleOp(name="answerKey",group="evolve") //Step 1, @Teleop, name & group, Okay to import
public class answerKey extends LinearOpMode{ //Step 2, extends..., light bulb>make abstract
    //
    DcMotor left;
    DcMotor right;
    TouchSensor wall; //Step 3, add Hardware
    IrSeekerSensor ir;
    //
    public void runOpMode() throws InterruptedException { //Step 4, runOpMode
        //
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        wall = hardwareMap.touchSensor.get("wall");
        ir = hardwareMap.irSeekerSensor.get("ir");
        //
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        //
        waitForStart();
        //
        while (opModeIsActive()){
            //
            if (!((-gamepad1.left_stick_y > 0 || -gamepad1.right_stick_y > 0) && wall.isPressed())) {
                left.setPower(-gamepad1.right_stick_y);
                right.setPower(-gamepad1.left_stick_y);
            }else{
                left.setPower(0);
                right.setPower(0);
            }
            //
            telemetry.addData("Wall: ", wall.isPressed());
            telemetry.addData("ir", ir.getStrength());
            telemetry.addData("ir2", ir.getAngle());
            telemetry.addData("left", gamepad1.right_stick_y);
            telemetry.addData("right", gamepad1.left_stick_y);
            telemetry.addData("go", (!(gamepad1.left_stick_y < 0 || gamepad1.right_stick_y < 0) && wall.isPressed()));
            telemetry.update();
            //
        }
        //
    }
}
