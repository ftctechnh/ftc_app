//* In order to break everything, remove one of the slashes. Your welcome.

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 10/1/2017!
 */

@TeleOp(name="basicDiagonal_mecanum_test",group="mecanum" )
@Disabled
public class basicDiagonal_mecanum_test extends LinearOpMode {
    HardwareSensorMap robot   = new HardwareSensorMap();   // Use a Pushbot's hardware  // no
    //power variables
    DcMotor leftFrontMotor;
    DcMotor rightFrontMotor;
    DcMotor leftBackMotor;
    DcMotor rightBackMotor;

    @Override
    public void runOpMode() throws InterruptedException {

        //get motors
        leftBackMotor = hardwareMap.dcMotor.get("m0"); //left back
        rightBackMotor = hardwareMap.dcMotor.get("m1"); //right back
        leftFrontMotor = hardwareMap.dcMotor.get("m2"); //left front
        rightFrontMotor = hardwareMap.dcMotor.get("m3"); //right front

        //neat variables
        double drive; //turn power
        double turn; //turn direction
        double power; //move power
        double leftX; //left x: joystick
        double diagonal = 0;
        double leftBackMotorPower; //leftBackMotor
        double rightBackMotorPower; //rightBackMotor
        double leftFrontMotorPower; //leftFrontMotor
        double rightFrontMotorPower; //rightMotorPower

        waitForStart(); //wait for the button to be pushed

        while (opModeIsActive()) {
            drive = gamepad1.left_stick_y; //left joystick moving up and down
            turn = gamepad1.right_stick_x; //right joystick left and right
            leftX = gamepad1.left_stick_x; //left joystick moving right and left
            power = getPythagoras(leftX, drive); //the current joystick position

            boolean turningRight = turn < 0;
            boolean notTurning = turn == 0;
            boolean movingVertical = Math.abs(drive) / Math.abs(leftX) > 4;
            boolean strafingRight = leftX > 0;
            //main if/then
            if (turningRight) {
                //
                //pushing right joystick to the right
                //turn right by left wheels going forward and right going backwards

                //
                turnRight(power);
            } else if (notTurning) {
                //no movement in right joystick

                //start of driving section
                if (movingVertical) { //forward/back or left/right?
                    diagonal = 0;
                    if (drive > 0) { //forward
                       driveForward(power);
                    } else { //back
                        driveBackward(power);
                    }
                } else {
                    if (drive != 0){
                        diagonal = 1;
                    }
                    if (strafingRight) { //right
                        strafeRight(Math.abs(leftX), drive);
                    } else { //left
                        strafeLeft(Math.abs(leftX), drive);
                    }
                }
            } else {
                //turn left
                turnLeft(power);
            }
            telemetry.addData("front left", leftFrontMotor.getPower());
            telemetry.addData("front right", rightFrontMotor.getPower());
            telemetry.addData("back left", leftBackMotor.getPower());
            telemetry.addData("back right", rightBackMotor.getPower());
            telemetry.addData("Diagonal?", diagonal);
            telemetry.update();
        }
    }

    private void turnRight(double power){
        leftBackMotor.setPower(power);
        leftFrontMotor.setPower(power);
        rightBackMotor.setPower(-power);
        rightFrontMotor.setPower(-power);
    }

    private void turnLeft(double power){
        leftBackMotor.setPower(-power);
        leftFrontMotor.setPower(-power);
        rightBackMotor.setPower(power);
        rightFrontMotor.setPower(power);
    }

    private void driveForward(double power){
        leftBackMotor.setPower(power);
        leftFrontMotor.setPower(power);
        rightBackMotor.setPower(power);
        rightFrontMotor.setPower(power);
    }

    private void driveBackward(double power){
        leftBackMotor.setPower(-power);
        leftFrontMotor.setPower(-power);
        rightBackMotor.setPower(-power);
        rightFrontMotor.setPower(-power);
    }

    private void strafeLeft(double power, double drive){ //this is where the trouble lies
        if (drive > 0){
            drive = Math.abs(drive);
            leftBackMotor.setPower(-(power - drive));
            leftFrontMotor.setPower(power);
            rightBackMotor.setPower(power);
            rightFrontMotor.setPower(-(power - drive));
        }else{
            drive = Math.abs(drive);
            leftBackMotor.setPower(-power);
            leftFrontMotor.setPower(power - drive);
            rightBackMotor.setPower(power - drive);
            rightFrontMotor.setPower(-power);
        }

    }

    private void strafeRight(double power, double drive){//this is also where the trouble lies
        if (drive > 0 ){
            drive = Math.abs(drive);
            leftBackMotor.setPower((power));
            leftFrontMotor.setPower(-(power - drive));
            rightBackMotor.setPower(-(power - drive));
            rightFrontMotor.setPower(-(power));
        }else{
            drive = Math.abs(drive);
            leftBackMotor.setPower(power);
            leftFrontMotor.setPower(-(power - drive));
            rightBackMotor.setPower(-(power - drive));
            rightFrontMotor.setPower(power);
        }

    }

    private double getPythagoras(double a, double b){//Define Pythagorean Theorem
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return c;

        //Finding the joystick position using pythagorean theorem (a^2 + b^2 = c^2)
    }


}
