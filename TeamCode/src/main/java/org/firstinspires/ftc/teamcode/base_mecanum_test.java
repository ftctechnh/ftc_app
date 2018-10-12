package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.relicrecovery.JeffThePengwin;

import static java.lang.Math.abs;

/**
 * Created by Eric on 9/20/2017!
 */

@TeleOp(name="base_mecanum_test",group="mecanum" )
@Disabled
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

        JeffThePengwin jeffThePengwin = new JeffThePengwin(hardwareMap);

        //neat variables
        double drive; //turn power
        double turn; //turn direction
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
            double power = getPathagorus(leftX, drive); //the current joystick position

            double degreePower = 1;
            if(gamepad1.left_bumper) {
                degreePower = 0.5;
            }
            else if(gamepad1.right_bumper){
                degreePower = 0.25;
            }

            jeffThePengwin.setDegreeOfPower(degreePower);
            jeffThePengwin.setPowerInput(power);
            boolean turningRight = turn < 0;
            boolean notTurning = turn == 0;
            boolean movingVertical = Math.abs(drive) > Math.abs(leftX);
            boolean strafingRight = leftX > 0;
            //main if/then
            if (notTurning) {
                //no movement in right joystick
                //start of driving section
                if (movingVertical) { //forward/back or left/right?
                    if (drive > 0) { //forward
                      jeffThePengwin.driveForward();
                    } else { //back
                        jeffThePengwin.driveBackward();
                    }
                } else {
                    if (strafingRight) { //right
                        jeffThePengwin.strafeRight();
                    } else { //left
                        jeffThePengwin.strafeLeft();
                    }
                }
            } else if (turningRight) {
                //pushing right joystick to the right
                //turn right by left wheels going forward and right going backwards
                jeffThePengwin.turnRight();
            } else {
                //turn left
                jeffThePengwin.turnLeft();
            }

            telemetry.addData("front left", leftFrontMotor.getPower());
            telemetry.addData("front right", rightFrontMotor.getPower());
            telemetry.addData("back left", leftBackMotor.getPower());
            telemetry.addData("back right", rightBackMotor.getPower());
            telemetry.addData("turn", gamepad1.right_stick_x);
            telemetry.update();
        }
    }

    private void findPosition( double position){
        turnyThing.setPower(90 - position);
    }

    private double getPathagorus(double a, double b){//Define Pythagorean Theorem
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return c;

        //Finding the joystick position using pythagorean theorem (a2 + b2 = c2)
    }


}
