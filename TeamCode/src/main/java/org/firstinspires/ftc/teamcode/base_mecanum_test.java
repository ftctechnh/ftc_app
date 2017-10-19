package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import static java.lang.Math.abs;

/**
 * Created by Eric on 9/20/2017!
 */

@TeleOp(name="base_mecanum_test",group="mecanum" )
public class base_mecanum_test extends LinearOpMode {
    HardwareSensorMap robot   = new HardwareSensorMap();   // Use a Pushbot's hardware
    //power variables
    DcMotor leftFrontMotor;
    DcMotor rightFrontMotor;
    DcMotor leftBackMotor;
    DcMotor rightBackMotor;
    DcMotor turnyThing;

    @Override
    public void runOpMode() throws InterruptedException {

        JeffThePenguin jeffThePenguin = new JeffThePenguin(hardwareMap);

        /*
        //get motors
        leftBackMotor = hardwareMap.dcMotor.get("m0"); //left back
        rightBackMotor = hardwareMap.dcMotor.get("m1"); //right back
        leftFrontMotor = hardwareMap.dcMotor.get("m2"); //left front
        rightFrontMotor = hardwareMap.dcMotor.get("m3"); //right front
        turnyThing = hardwareMap.dcMotor.get("m4");
        */

        //neat variables
        double drive; //turn power
        double turn; //turn direction
        double power; //move power
        double leftX; //left x: joystick
        boolean move;
        double position;
        boolean done;

        done = true;
        position = 0;

        waitForStart(); //wait for the button to be pushed

        while (opModeIsActive()) {
            drive = gamepad1.left_stick_y; //left joystick moving up and down
            turn = gamepad1.right_stick_x; //right joystick left and right
            leftX = gamepad1.left_stick_x; //left joystick moving right and left
            move = gamepad2.b;
            //
            //
            if (move && done){
                if (position == 90){
                    findPosition(position);
                }else{
                    findPosition(position);
                }

            }

            //
            power = getPathagorus(leftX, drive); //the current joystick position

            double degreePower = 1;
            if(gamepad1.left_bumper) {
                degreePower = 0.5;
            }
            else if(gamepad1.right_bumper){
                degreePower = 0.25;
            }
            power = degreePower*power;
            boolean turningRight = turn < 0; //TODO This is not working right, it should be > but it is mixing up and left and righ
            boolean notTurning = turn == 0;
            boolean movingVertical = Math.abs(drive) > Math.abs(leftX);
            boolean strafingRight = leftX > 0;
            //main if/then
            if (notTurning) {
                //no movement in right joystick
                //start of driving section
                if (movingVertical) { //forward/back or left/right?
                    if (drive > 0) { //forward
                        telemetry.addData("I say ", "go foward");
                      jeffThePenguin.driveForward(power);
                    } else { //back
                        telemetry.addData("I say ", "go backward");
                        jeffThePenguin.driveBackward(power);
                    }
                } else {
                    if (strafingRight) { //right
                        telemetry.addData("I say ", "strafe right");
                        jeffThePenguin.strafeRight(power);
                    } else { //left
                        telemetry.addData("I say ", "strafe left");
                        jeffThePenguin.strafeLeft(power);
                    }
                }
            } else if (turningRight) {
                //pushing right joystick to the right
                //turn right by left wheels going forward and right going backwards
                telemetry.addData("I say ", "turn right");
                jeffThePenguin.turnRight(power);
            } else {
                //turn left
                telemetry.addData("I say ", "turn left");
                jeffThePenguin.turnLeft(power);
            }

            telemetry.addData("front left", leftFrontMotor.getPower());
            telemetry.addData("front right", rightFrontMotor.getPower());
            telemetry.addData("back left", leftBackMotor.getPower());
            telemetry.addData("back right", rightBackMotor.getPower());
            telemetry.addData("turn", gamepad1.right_stick_x);
            telemetry.update();
        }
    }
    /*
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

    private void strafeLeft(double power){
        leftBackMotor.setPower(-power);
        leftFrontMotor.setPower(power);
        rightBackMotor.setPower(power);
        rightFrontMotor.setPower(-power);
    }

    private void strafeRight(double power){
        leftBackMotor.setPower(power);
        leftFrontMotor.setPower(-power);
        rightBackMotor.setPower(-power);
        rightFrontMotor.setPower(power);
    }
    */

    private void findPosition( double position){
        turnyThing.setPower(90 - position);
    }

    private double getPathagorus(double a, double b){//Define Pythagorean Theorem
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return c;

        //Finding the joystick position using pythagorean theorem (a2 + b2 = c2)
    }


}
