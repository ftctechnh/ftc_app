package org.firstinspires.ftc.teamcode.evolve;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Eric on 6/10/2018.
 */

@Autonomous(name="testAuto",group="evolve") //Step 1, @Teleop, name & group, Okay to import
public class testAuto extends LinearOpMode{ //Step 2, extends..., light bulb>make abstract
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
        wall = hardwareMap.touchSensor.get("wall");
        //
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        //
        waitForStart();
        //
        if (ir.getAngle() > 0){
            turn(-1);
            while(ir.getAngle() != 0){}
            turn(0);
        }else if (ir.getAngle() <= 0){
            turn(1);
            while (ir.getAngle() != 0){}
            turn(0);
        }else{
            turn(1);
            while (!ir.signalDetected()){}
            turn(0);
            //
            if (ir.getAngle() > 0){
                turn(-1);
                while(ir.getAngle() != 0){}
                turn(0);
            }else if (ir.getAngle() <= 0){
                turn(1);
                while (ir.getAngle() != 0){}
                turn(0);
            }
        }
        //
        //put stuff here
    }
    //
    public void turn(double power){
        left.setPower(power);
        right.setPower(-power);
    }
}
