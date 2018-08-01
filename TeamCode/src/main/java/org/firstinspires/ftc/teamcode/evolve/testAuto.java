package org.firstinspires.ftc.teamcode.evolve;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.text.DecimalFormat;

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
        ir = hardwareMap.irSeekerSensor.get("ir");
        //
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        //
        waitForStart();
        //
        //while (opModeIsActive()) {
            //
            if (ir.getAngle() > 0) {
                find(1);
            } else if (ir.getAngle() <= 0) {
                find(-1);
            } else {
                turn(-.1);
                telemetry.addData("no signal", "");
                telemetry.update();
                //
                while (!ir.signalDetected()) {}
                turn(0);
                //
                if (ir.getAngle() > 0) {
                    find(1);
                } else if (ir.getAngle() <= 0) {
                    find(-1);
                }
            //}
            //
            left.setPower(.1);
            right.setPower(.1);
            //
            //
            while (ir.getStrength() < .25 && opModeIsActive()) {
                telemetry.addData("done!", "");
                telemetry.addData("ir", ir.getStrength());
                telemetry.update();
            }
            //
            left.setPower(0);
            right.setPower(0);
        }
    }

    private boolean keepTurning() {
        return
                 (!withinRange((int)(ir.getAngle()))
                || ir.getStrength() < .05
                && opModeIsActive());
    }

    //
    public void turn(double power){
        left.setPower(-power);
        right.setPower(power);
    }
    //
    public boolean withinRange(int dir){
        boolean withinRange = false;
        //
        if (-10 < dir && dir < 10){
            withinRange = true;
        }
        //
        return withinRange;
    }
    //
    public void find(int direction){
        turn(.1 * direction);
        telemetry.addData("direction", direction);
        telemetry.update();
        //
        while (keepTurning()) {
            if (-100 < ir.getAngle() && ir.getAngle() < 100 && ir.getStrength() > 0.5){
                turn(.1 * direction * (ir.getAngle() / (100 * direction)));
                telemetry.addData("power", ir.getAngle() / (100 * direction));
            }else{
                turn(.1 * direction);
                telemetry.addData("power", 1);
            }
            telemetry.addData("angle", ir.getAngle());
            telemetry.addData("stop?", withinRange((int)ir.getAngle()));
            telemetry.update();
        }
        turn(0);
    }
}
