package org.firstinspires.ftc.teamcode.evolve;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.HardwareSensorMap;

/**
 * Created by Eric on 6/10/2018.
 */

@TeleOp(name="testClass",group="evolve") //Step 1, @Teleop, name & group, Okay to import
public abstract class testClass extends LinearOpMode{ //Step 2, extends..., light bulb>make abstract
    //
    DcMotor left;
    DcMotor right;
    TouchSensor wall; //Step 3, add Hardware
    //
    public void runOpMode() throws InterruptedException { //Step 4, runOpMode
        //
        hardwareMap.dcMotor.get("left");
        hardwareMap.dcMotor.get("right");
        hardwareMap.touchSensor.get("wall");
        //
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        //
        waitForStart();
        //
        while (opModeIsActive()){
            //
            left.setPower(gamepad1.left_stick_y);
            right.setPower(gamepad1.right_stick_y);
            //
            if (wall.isPressed()){
                telemetry.addData("Touching wall.", "");
            }else{
                telemetry.addData("Nothing detected.", "");
            }
            //
        }
        //
    }
}
