package org.firstinspires.ftc.teamcode.evolve;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Eric on 6/10/2018.
 */

@Autonomous(name="followAuto",group="evolve") //Step 1, @Teleop, name & group, Okay to import
public class followAuto extends LinearOpMode{ //Step 2, extends..., light bulb>make abstract
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
        staticTurn();
        //
        while (opModeIsActive()){
            //
            if (inRange()){
                moveTurn();
            }else{
                staticTurn();
            }
            //
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
        if (-100 < ir.getAngle() && ir.getAngle() < 100 && ir.getStrength() > 0.5){
            turn(.1 * direction * (ir.getAngle() / (100 * direction)));
            telemetry.addData("power", ir.getAngle() / (100 * direction));
        }else{
            turn(.1 * direction);
            telemetry.addData("power", 1);
        }
    }
    //
    public void staticTurn () {
        if (ir.getStrength() > .2) {
            turn(0);
        } else if (ir.getAngle() < -10) {
            find(-1);
        }else if (ir.getAngle() > 10){
            find(1);
        } else {
            turn(-.1);
        }
    }
    //
    public boolean inRange(){
        boolean inRange = false;
        Double dir = ir.getAngle();
        //
        if ((-50 < dir && dir < 50) && ir.getStrength() < .2 && ir.getStrength() > .05){
            inRange =true;
        }
        //
        return inRange;
    }
    //
    public void moveTurn(){
        if (ir.getAngle() < 0) {
            right.setPower(.2);
            left.setPower((1 - (ir.getAngle() / -360)) / 5);
        } else {
            left.setPower(.2);
            right.setPower((1 - (ir.getAngle() / 360)) / 5);
        }
    }
}
